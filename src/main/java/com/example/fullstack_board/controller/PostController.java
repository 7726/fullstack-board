package com.example.fullstack_board.controller;

import com.example.fullstack_board.dto.CommonPageResponse;
import com.example.fullstack_board.dto.PostRequest;
import com.example.fullstack_board.dto.PostResponse;
import com.example.fullstack_board.dto.PostUpdateRequest;
import com.example.fullstack_board.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public PostResponse update(@PathVariable Long id, @RequestBody PostUpdateRequest request) {
        return postService.update(id, request);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }

    @Operation(summary = "게시글 키워드 검색")
    @GetMapping("/search")
    public CommonPageResponse<PostResponse> search(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        return postService.searchByKeyword(keyword, pageable);
    }

}
