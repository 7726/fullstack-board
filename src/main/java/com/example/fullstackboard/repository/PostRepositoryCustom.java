package com.example.fullstackboard.repository;

import com.example.fullstackboard.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PostRepositoryCustom {
    Page<Post> searchByKeyword(String keyword, Pageable pageable);

    Page<Post> searchByCondition(String keyword,
                               String authorEmail,
                               LocalDateTime startDateTime,
                               LocalDateTime endDateTime,
                               Pageable pageable);
}
