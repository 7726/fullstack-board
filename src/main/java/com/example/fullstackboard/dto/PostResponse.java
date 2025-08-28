package com.example.fullstackboard.dto;

import java.time.LocalDateTime;

public record PostResponse(
   Long id,
   String title,
   String content,
   Long authorId,
   String authorEmail,
   LocalDateTime createdAt
) {}
