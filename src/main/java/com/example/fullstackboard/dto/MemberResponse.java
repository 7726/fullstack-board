package com.example.fullstackboard.dto;

import com.example.fullstackboard.domain.Role;

import java.time.LocalDateTime;

public record MemberResponse (
    Long id,
    String email,
    Role role,
    LocalDateTime createdAt
) {}
