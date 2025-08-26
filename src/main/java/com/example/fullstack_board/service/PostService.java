package com.example.fullstack_board.service;

import com.example.fullstack_board.domain.Member;
import com.example.fullstack_board.domain.Post;
import com.example.fullstack_board.dto.*;
import com.example.fullstack_board.repository.MemberRepository;
import com.example.fullstack_board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 게시글 작성
    @Transactional
    public PostResponse create(PostRequest req) {
        // 작성자 조회
        Member author = memberRepository.findById(req.memberId())
                .orElseThrow(() -> new IllegalArgumentException("작성자가 존재하지 않습니다."));

        Post saved = postRepository.save(
                Post.builder()
                        .title(req.title())
                        .content(req.content())
                        .member(author)
                        .build()
        );
        return toResponse(saved);
    }

    // 게시글 단건 조회
    @Transactional(readOnly = true)
    public PostResponse getById(Long id) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return toResponse(p);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponse> getAll() {
        return postRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // 게시글 수정
    @Transactional
    public PostResponse update(Long id, PostUpdateRequest req) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        boolean nothingToUpdate = (req.title() == null || req.title().isBlank())
                && (req.content() == null || req.content().isBlank());
        if (nothingToUpdate) {
            throw new IllegalArgumentException("수정할 값이 없습니다.");
        }

        if (req.title() != null && !req.title().isBlank()) {
            p = Post.builder()
                    .id(p.getId())
                    .title(req.title())
                    .content(p.getContent())
                    .member(p.getMember())
                    .createdAt(p.getCreatedAt())
                    .build();
            // 엔티티를 불변으로 두고 싶을 때 빌더 재생성 방식을 쓰기도 하지만,
            // 지금은 간단히 필드 세터 없이 빌더-재생성 패턴을 보여줌
            // 실무에선 setter or 별도 update 메서드로 처리하는 패턴도 흔함.
            // (아래 간단 버전으로 다시 할당)
            p = postRepository.save(p);
        }
        if (req.content() != null && !req.content().isBlank()) {
            p = Post.builder()
                    .id(p.getId())
                    .title((p.getTitle()))
                    .content(req.content())
                    .member(p.getMember())
                    .createdAt(p.getCreatedAt())
                    .build();
            p = postRepository.save(p);
        }

        return toResponse(p);
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        postRepository.delete(p);  // soft delete로 바꿀 예정
    }

    // 페이징/정렬
    @Transactional(readOnly = true)
    public CommonPageResponse<PostResponse> getPaged(Pageable pageable) {
        Page<Post> page = postRepository.findAll(pageable);

        List<PostResponse> rows = page.getContent().stream()
                .map(this::toResponse)
                .toList();

        PageMetaResponse meta = new PageMetaResponse(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
        return new CommonPageResponse<>(rows, meta);
    }

    // 게시글 키워드 검색
    @Transactional(readOnly = true)
    public CommonPageResponse<PostResponse> searchByKeyword(String keyword, Pageable pageable) {
        Page<Post> page = postRepository.searchByKeyword(keyword, pageable);

        List<PostResponse> rows = page.getContent().stream()
                .map(this::toResponse)
                .toList();

        PageMetaResponse meta = new PageMetaResponse(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
        return new CommonPageResponse<>(rows, meta);
    }

    private PostResponse toResponse(Post p) {
        return new PostResponse(
                p.getId(),
                p.getTitle(),
                p.getContent(),
                p.getMember().getId(),
                p.getMember().getEmail(),
                p.getCreatedAt()
        );
    }

}
