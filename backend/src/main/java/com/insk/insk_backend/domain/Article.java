package com.insk.insk_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "articles") // ERD의 테이블 이름
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id") // ERD의 컬럼 이름 (article_id)
    private Long articleId; // Dto에서 getArticleId()를 호출

    @Column(nullable = false)
    private String title;

    @Column(name = "original_url", nullable = false, unique = true, length = 512)
    private String originalUrl;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // [⭐️⭐️⭐️ 수정 ⭐️⭐️⭐️]
    // 'category' 필드는 이 엔티티에 없습니다. (제거)

    @Builder
    public Article(String title, String originalUrl, LocalDateTime publishedAt, LocalDateTime createdAt) {
        this.title = title;
        this.originalUrl = originalUrl;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
    }
}