package com.example.fullstack_board.dto;

import com.example.fullstack_board.domain.Role;

import java.time.LocalDateTime;

public record MemberResponse (
    Long id,
    String email,
    Role role,
    LocalDateTime createdAt
) {}
