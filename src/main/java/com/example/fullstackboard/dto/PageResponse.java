package com.example.fullstackboard.dto;

import org.springframework.data.domain.Page;

import java.util.function.Function;

public class PageResponse {

    // Page 객체(스프링이 주는 페이지 결과)를
    // -> 우리가 만든 CommonPageResponse 형태로 바꿔주는 메서드
    public static <T, R> CommonPageResponse<R> of(Page<T> page, Function<T, R> mapper) {
        // 엔티티 리스트(page의 content)를 DTO 등으로 변환
        var data = page.getContent().stream().map(mapper).toList();

        // 페이징 관련 정보 담기
        var meta = new PageMetaResponse(
                page.getNumber(),  // 현재 페이지 번호 (0시작)
                page.getSize(),  // 페이지 크기
                page.getTotalElements(),  // 전체 데이터 개수
                page.getTotalPages(),  // 전체 페이지 수
                page.isFirst(),  // 첫 페이지 여부
                page.isLast()  // 마지막 페이지 여부
        );

        // data(실제 결과) + meta(페이징 정보) 조합해서 반환
        return new CommonPageResponse<>(data, meta);
    }
}
