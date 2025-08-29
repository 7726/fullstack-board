package com.example.fullstackboard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "posts")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Lob  // 긴 글, 본문 저장용 어노테이션
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;  // 작성자

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isDeleted;

    // 디폴트 값
    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    // 부분 수정
    public void update(String title, String content) {
        if (title != null && !title.isBlank()) this.title = title;
        if (content != null && !content.isBlank()) this.content = content;
    }

    // 소프트 삭제
    public void softDelete() {
        this.isDeleted = true;
    }


}
