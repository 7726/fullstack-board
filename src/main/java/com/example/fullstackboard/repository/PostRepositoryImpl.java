package com.example.fullstackboard.repository;

import com.example.fullstackboard.domain.Post;
import com.example.fullstackboard.domain.QMember;
import com.example.fullstackboard.domain.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> searchByCondition(String keyword,
                                        String authorEmail,
                                        LocalDateTime startDateTime,
                                        LocalDateTime endDateTime,
                                        Pageable pageable) {
        QPost post = QPost.post;
        QMember member = QMember.member;

        BooleanBuilder where = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            where.and(post.title.containsIgnoreCase(keyword));
        }
        if (authorEmail != null && !authorEmail.isBlank()) {
            where.and(member.email.equalsIgnoreCase(authorEmail));
        }
        if (startDateTime != null) {
            where.and(post.createdAt.goe(startDateTime));
        }
        if (endDateTime != null) {
            where.and(post.createdAt.loe(endDateTime));
        }

        where.and(post.isDeleted.isFalse());

        List<Post> rows = queryFactory
                .selectFrom(post)
                .join(member).on(member.eq(post.member())).fetchJoin()
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .join(member).on(member.eq(post.member()))
                .where(where)
                .fetchOne();

        return PageableExecutionUtils.getPage(rows, pageable, () -> total == null ? 0 : total);
    }

    @Override
    public Page<Post> searchByKeyword(String keyword, Pageable pageable) {
        QPost post = QPost.post;

        BooleanExpression condition = keyword != null && !keyword.isBlank()
                ? post.title.containsIgnoreCase(keyword)
                : null;

        condition.and(post.isDeleted.isFalse());

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
