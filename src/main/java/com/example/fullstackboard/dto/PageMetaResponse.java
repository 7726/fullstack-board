package com.example.fullstackboard.dto;

public record PageMetaResponse (
   int page,  // 현재 페이지 (0부터 시작)
   int size,  // 페이지 크기
   long totalElements,
   int totalPages,
   boolean first,
   boolean last
) {}

