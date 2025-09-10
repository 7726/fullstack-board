package com.example.fullstackboard.dto;

import com.example.fullstackboard.domain.Role;
import com.example.fullstackboard.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.time.LocalDateTime;

public record MemberResponse (
    Long id,
    String email,
    Role role,
    LocalDateTime createdAt
) {}
