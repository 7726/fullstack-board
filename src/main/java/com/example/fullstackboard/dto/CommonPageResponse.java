package com.example.fullstackboard.dto;

import java.util.List;

// List<T> data: 실제 데이터를 여러 개 담는 리스트
// PageMetaResponse meta: 지금이 몇 페이지인지, 전체는 몇 페이지인지 등 "페이지에 대한 정보"를 담는 객체
public record CommonPageResponse<T> (List<T> data, PageMetaResponse meta) {}
