package com.example.fullstackboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// PATCH용: null인 필드는 미수정
public record PostUpdateRequest(
    @Schema(description = "수정할 제목", example = "제목 수정본")
    String title,
    @Schema(description = "수정할 내용", example = "내용 수정본")
    String content
) {}
