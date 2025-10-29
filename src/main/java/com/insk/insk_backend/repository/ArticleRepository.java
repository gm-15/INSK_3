package com.insk.insk_backend.repository;

import com.insk.insk_backend.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * NewsPipelineService에서 중복 기사 체크 시 사용
     */
    boolean existsByOriginalUrl(String originalUrl);
}