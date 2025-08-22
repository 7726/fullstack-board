package com.example.fullstack_board.controller;

import com.example.fullstack_board.dto.PostRequest;
import com.example.fullstack_board.dto.PostResponse;
import com.example.fullstack_board.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "게시글 전체 조회")
    @GetMapping
    public List<PostResponse> getAll() {
        return postService.getAll();
    }

}
