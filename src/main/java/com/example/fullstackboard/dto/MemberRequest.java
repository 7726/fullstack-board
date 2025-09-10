package com.example.fullstackboard.dto;

import com.example.fullstackboard.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest (
        @Schema(description = "회원 이메일", example = "user1@example.com")
        @Email @NotBlank String email,

        @Schema(description = "회원 비밀번호(BCrypt 해시 저장)", example = "1234")
        @NotBlank String password,

        @Schema(description = "회원 권한", example = "USER")
        Role role  // null 이면 USER로 기본 처리
) {}
