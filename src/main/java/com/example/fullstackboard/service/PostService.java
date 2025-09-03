package com.example.fullstackboard.service;

import com.example.fullstackboard.domain.Member;
import com.example.fullstackboard.domain.Post;
import com.example.fullstackboard.dto.*;
import com.example.fullstackboard.exception.BadRequestException;
import com.example.fullstackboard.exception.NotFoundException;
import com.example.fullstackboard.repository.MemberRepository;
import com.example.fullstackboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                .orElseThrow(() -> new NotFoundException("작성자가 존재하지 않습니다."));

        Post saved = postRepository.save(
                Post.builder()
                        .title(req.title())
                        .content(req.content())
                        .member(author)
                        .build()
        );
        return toResponse(saved);
    }

    // 조회 공통 람다(중복 제거) - 선택
    private Post getActivePostOrThrow(Long id) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));
        if (p.isDeleted()) throw new BadRequestException("삭제된 게시글입니다.");

        return p;
    }

    // 게시글 단건 조회
    @Transactional(readOnly = true)
    public PostResponse getById(Long id) {
        return toResponse(getActivePostOrThrow(id));
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponse> getAll() {
        return postRepository.findAll().stream()
                .filter(p -> !p.isDeleted())
                .map(this::toResponse)
                .toList();
    }

    // 게시글 수정
    @Transactional
    public PostResponse update(Long id, PostUpdateRequest req) {
        boolean nothingToUpdate = (req.title() == null || req.title().isBlank())
                && (req.content() == null || req.content().isBlank());
        if (nothingToUpdate) {
            throw new BadRequestException("수정할 값이 없습니다.");
        }

        Post p = getActivePostOrThrow(id);

        p.update(req.title(), req.content());

        return toResponse(p);
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));
        if (!p.isDeleted()) p.softDelete();
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
        return PageResponse.of(page, this::toResponse);
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
        return PageResponse.of(page, this::toResponse);
    }

    @Transactional(readOnly = true)
    public CommonPageResponse<PostResponse> searchAdvanced(
            String keyword,
            String authorEmail,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Pageable pageable
    ) {
        Page<Post> page = postRepository.searchByCondition(
                keyword, authorEmail, startDateTime, endDateTime, pageable
        );

        List<PostResponse> data = page.getContent().stream()
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
        return PageResponse.of(page, this::toResponse);
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
