package com.example.fullstackboard.controller;

import com.example.fullstackboard.dto.CommonPageResponse;
import com.example.fullstackboard.dto.PostRequest;
import com.example.fullstackboard.dto.PostResponse;
import com.example.fullstackboard.dto.PostUpdateRequest;
import com.example.fullstackboard.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 등록")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody PostRequest request) {
        return postService.create(request);
    }

    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/{id}")
    public PostResponse getById(@PathVariable Long id) {
        return postService.getById(id);
    }

    @Operation(summary = "게시글 전체 조회 (단순 리스트")
    @GetMapping
    public List<PostResponse> getAll() {
        return postService.getAll();
    }

    @Operation(summary = "게시글 페이징 조회 (최신순)")
    @GetMapping("/paged")
    public CommonPageResponse<PostResponse> getPaged(
            @PageableDefault(sort = "createdAt", direction = DESC, size = 10) Pageable pageable
    ) {
        return postService.getPaged(pageable);
    }

    @Operation(summary = "게시글 수정")
    @PatchMapping("/{id}")
    public PostResponse update(@PathVariable Long id,
                               @RequestBody PostUpdateRequest request,
                               Authentication auth) {
        // 현재 로그인한 사용자 이메일 꺼내기
        String currentEmail = (String) auth.getPrincipal();
        return postService.update(id, request, currentEmail);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       Authentication auth) {
        String currentEmail = (String) auth.getPrincipal();
        postService.delete(id, currentEmail);
    }

    @Operation(summary = "게시글 키워드 검색")
    @GetMapping("/search")
    public CommonPageResponse<PostResponse> search(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        return postService.searchByKeyword(keyword, pageable);
    }

    @Operation(summary = "게시글 복합 조건 검색")
    @GetMapping("/advanced-search")
    public CommonPageResponse<PostResponse> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String authorEmail,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        // 날짜는 하루의 시작~끝으로 보정
        LocalDateTime startDt = (startDate == null) ? null : startDate.atStartOfDay();
        LocalDateTime endDt = (endDate == null) ? null : endDate.atTime(LocalTime.MAX);

        return postService.searchAdvanced(keyword, authorEmail, startDt, endDt, pageable);
    }

}
