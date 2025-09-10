package com.example.fullstackboard.controller;

import com.example.fullstackboard.config.jwt.JwtTokenProvider;
import com.example.fullstackboard.domain.Member;
import com.example.fullstackboard.exception.BadRequestException;
import com.example.fullstackboard.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "로그인", description = "JWT 토큰 발급")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
        content = @Content(schema = @Schema(example = "{ \"token\": \"eyJhbGciOiJI...\" }")))
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String rawPassword = req.get("password");

        // DB에서 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 회원입니다."));

        // 평문(raw) vs 해시(encoded) 비교는 matches 사용
        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new BadRequestException("비밀번호가 올바르지 않습니다.");
        }

        // JWT 토큰 발급
        String token = jwtTokenProvider.createToken(email, member.getRole().name());

        return ResponseEntity.ok(Optional.of(Map.of("token", token)));
    }

}
