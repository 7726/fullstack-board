package com.example.fullstack_board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequest(
   @NotBlank String title,
   @NotBlank String content,
   @NotNull Long memberId  // 작성자 ID
) {}
