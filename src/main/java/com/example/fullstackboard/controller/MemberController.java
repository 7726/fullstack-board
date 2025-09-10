package com.example.fullstackboard.controller;

import com.example.fullstackboard.dto.MemberRequest;
import com.example.fullstackboard.dto.MemberResponse;
import com.example.fullstackboard.exception.ErrorResponse;
import com.example.fullstackboard.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 API", description = "회원 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "신규 회원을 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse register(@Valid @RequestBody MemberRequest request) {
        return memberService.register(request);
    }

    @Operation(summary = "회원 단건 조회")
    @GetMapping("/{id}")
    public MemberResponse getById(@PathVariable Long id) {
        return memberService.getById(id);
    }

    @Operation(summary = "본인 조회", description = "현재 로그인한 사용자의 정보를 조회한다.")
    @GetMapping("/me")
    public MemberResponse getMe(Authentication auth) {
        String currentEmail = (String) auth.getPrincipal();  // 현재 로그인한 사용자의 이메일
        return memberService.getMe(currentEmail);
    }
}
