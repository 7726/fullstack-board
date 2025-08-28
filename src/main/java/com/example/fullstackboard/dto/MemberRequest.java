package com.example.fullstackboard.dto;

import com.example.fullstackboard.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest (
        @Email @NotBlank String email,
        @NotBlank String password,
        Role role  // null 이면 USER로 기본 처리
) {}
