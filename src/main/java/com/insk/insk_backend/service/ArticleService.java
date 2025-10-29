package com.insk.insk_backend.service;

import com.insk.insk_backend.domain.Article;
import com.insk.insk_backend.domain.ArticleAnalysis;
import com.insk.insk_backend.dto.ArticleDto;
import com.insk.insk_backend.repository.ArticleAnalysisRepository;
import com.insk.insk_backend.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleAnalysisRepository articleAnalysisRepository;

    /**
     * [ ⭐️⭐️⭐️ 최종 수정본 ⭐️⭐️⭐️ ]
     * 뉴스 목록 조회 로직 (올바른 페이징 및 필터링)
     */
    public Page<ArticleDto.Response> getArticles(String category, Pageable pageable) {

        if (category == null || category.isEmpty()) {
            // 1. 카테고리 필터가 없으면 -> ArticleRepository에서 페이징
            Page<Article> articlesPage = articleRepository.findAll(pageable);

            // Page<Article>을 Page<ArticleDto.Response>로 변환
            return articlesPage.map(article -> {
                ArticleAnalysis analysis = articleAnalysisRepository.findByArticle(article)
                        .orElse(null); // .orElse(new ArticleAnalysis()) -> null 처리
                return ArticleDto.Response.from(article, analysis);
            });
        } else {
            // 2. 카테고리 필터가 있으면 -> ArticleAnalysisRepository에서 페이징
            // [ ⭐️⭐️⭐️ 핵심 수정 ⭐️⭐️⭐️ ]
            // articleRepository.findByCategory(category, pageable);
            // -> articleAnalysisRepository.findByCategory(category, pageable);
            Page<ArticleAnalysis> analysesPage = articleAnalysisRepository.findByCategory(category, pageable);

            // Page<ArticleAnalysis>를 Page<ArticleDto.Response>로 변환
            return analysesPage.map(analysis -> {
                // ArticleAnalysis에서 Article을 가져옴
                Article article = analysis.getArticle();
                return ArticleDto.Response.from(article, analysis);
            });
        }
    }

    /**
     * 뉴스 상세 조회 로직 (기존과 동일)
     */
    public ArticleDto.DetailResponse getArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("기사를 찾을 수 없습니다: " + articleId));

        ArticleAnalysis analysis = articleAnalysisRepository.findByArticle(article)
                .orElseThrow(() -> new EntityNotFoundException("기사 분석 결과를 찾을 수 없습니다: " + articleId));

        return ArticleDto.DetailResponse.from(article, analysis);
    }
}