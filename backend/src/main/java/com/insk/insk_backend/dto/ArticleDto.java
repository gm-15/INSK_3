package com.insk.insk_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.insk.insk_backend.domain.Article;
import com.insk.insk_backend.domain.ArticleAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable; // 1. Serializable 임포트 확인
import java.time.LocalDateTime;

public class ArticleDto {

    /**
     * GET /api/v1/articles (뉴스 목록 조회)를 위한 응답 DTO
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // 2. "implements Serializable" 구현 확인
    public static class Response implements Serializable {

        private Long articleId;
        private String title;
        private String summary;
        private String category;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime publishedAt;

        // from 메소드
        public static Response from(Article article, ArticleAnalysis analysis) {
            if (analysis == null) {
                return Response.builder()
                        .articleId(article.getArticleId())
                        .title(article.getTitle())
                        .summary("분석 중입니다...")
                        .category("분류 중...")
                        .publishedAt(article.getPublishedAt())
                        .build();
            }
            return Response.builder()
                    .articleId(article.getArticleId())
                    .title(article.getTitle())
                    .summary(analysis.getSummary())
                    .category(analysis.getCategory())
                    .publishedAt(article.getPublishedAt())
                    .build();
        }
    }

    /**
     * GET /api/v1/articles/{articleId} (뉴스 상세 조회)를 위한 응답 DTO
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    // 3. "implements Serializable" 구현 확인
    public static class DetailResponse implements Serializable {

        private Long articleId;
        private String title;
        private String originalUrl;
        private String summary;
        private String insight;
        private String category;
        private String tags;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime publishedAt;

        // from 메소드
        public static DetailResponse from(Article article, ArticleAnalysis analysis) {
            return DetailResponse.builder()
                    .articleId(article.getArticleId())
                    .title(article.getTitle())
                    .originalUrl(article.getOriginalUrl())
                    .publishedAt(article.getPublishedAt())
                    .summary(analysis.getSummary())
                    .insight(analysis.getInsight())
                    .category(analysis.getCategory())
                    .tags(analysis.getTags())
                    .build();
        }
    }
}
