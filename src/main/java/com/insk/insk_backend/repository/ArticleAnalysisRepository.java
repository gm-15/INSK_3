package com.insk.insk_backend.repository;

import com.insk.insk_backend.domain.Article;
import com.insk.insk_backend.domain.ArticleAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleAnalysisRepository extends JpaRepository<ArticleAnalysis, Long> {

    /**
     * ArticleService에서 기사 상세 조회 시 사용
     */
    Optional<ArticleAnalysis> findByArticle(Article article);

    /**
     * [ ⭐️⭐️⭐️ 이 메소드가 필요합니다 ⭐️⭐️⭐️ ]
     * ArticleService에서 카테고리 필터링 시 사용할 쿼리 메소드입니다.
     */
    Page<ArticleAnalysis> findByCategory(String category, Pageable pageable);
}