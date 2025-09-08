package com.example.fullstackboard.controller;

import com.example.fullstackboard.dto.MemberRequest;
import com.example.fullstackboard.dto.MemberResponse;
import com.example.fullstackboard.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 등록")
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

    @GetMapping("/me")
    public MemberResponse getMe(Authentication auth) {
        String currentEmail = (String) auth.getPrincipal();  // 현재 로그인한 사용자의 이메일
        return memberService.getMe(currentEmail);
    }
}
