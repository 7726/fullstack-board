package com.example.fullstack_board.service;

import com.example.fullstack_board.domain.Member;
import com.example.fullstack_board.domain.Post;
import com.example.fullstack_board.dto.PostRequest;
import com.example.fullstack_board.dto.PostResponse;
import com.example.fullstack_board.repository.MemberRepository;
import com.example.fullstack_board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

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

    @Transactional(readOnly = true)
    public PostResponse getById(Long id) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAll() {
        return postRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
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
