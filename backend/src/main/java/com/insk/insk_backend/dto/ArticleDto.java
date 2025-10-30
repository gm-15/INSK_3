package com.insk.insk_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.insk.insk_backend.domain.Article;
import com.insk.insk_backend.domain.ArticleAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ArticleDto {

    /**
     * GET /api/v1/articles (뉴스 목록 조회)를 위한 응답 DTO
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long articleId;
        private String title;
        private String summary; // AI 요약
        private String category;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime publishedAt;



        /**
         * [ ⭐️⭐️⭐️ 이 부분이 수정되었습니다 ⭐️⭐️⭐️ ]
         * ArticleAnalysis가 null일 경우를 대비하여 null-safe하게 수정
         */
        public static Response from(Article article, ArticleAnalysis analysis) {

            // 분석 결과가 아직 없는 경우 (analysis가 null일 때)
            if (analysis == null) {
                return Response.builder()
                        .articleId(article.getArticleId()) // Article.java에 getArticleId() 있음
                        .title(article.getTitle())
                        .summary("분석 중입니다...")
                        // [ ⭐️⭐️⭐️ 핵심 수정 ⭐️⭐️⭐️ ]
                        // article.getCategory() -> "분류 중..."으로 변경
                        .category("분류 중...")
                        .publishedAt(article.getPublishedAt())
                        .build();
            }

            // 분석 결과가 있는 경우 (기존 로직)
            return Response.builder()
                    .articleId(article.getArticleId())
                    .title(article.getTitle())
                    .summary(analysis.getSummary()) // 분석 테이블의 요약 사용
                    .category(analysis.getCategory()) // ArticleAnalysis의 카테고리 사용
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
    public static class DetailResponse {
        private Long articleId;
        private String title;
        private String originalUrl;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime publishedAt;
        private String summary;
        private String insight;
        private String category;
        private String tags; // JSON 문자열 형태


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