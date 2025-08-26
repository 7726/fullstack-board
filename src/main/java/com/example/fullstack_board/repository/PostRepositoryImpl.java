package com.example.fullstack_board.repository;

import com.example.fullstack_board.domain.Post;
import com.example.fullstack_board.domain.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> searchByKeyword(String keyword, Pageable pageable) {
        QPost post = QPost.post;

        BooleanExpression condition = keyword != null && !keyword.isBlank()
                ? post.title.containsIgnoreCase(keyword)
                : null;

        List<Post> results = queryFactory
                .selectFrom(post)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();

        // count 쿼리
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(condition)
                .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> total == null ? 0 : total);
    }
}
