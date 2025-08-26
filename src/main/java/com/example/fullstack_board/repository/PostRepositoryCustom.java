package com.example.fullstack_board.repository;

import com.example.fullstack_board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> searchByKeyword(String keyword, Pageable pageable);
}
