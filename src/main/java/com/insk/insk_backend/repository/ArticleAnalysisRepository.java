package com.insk.insk_backend.repository;

import com.insk.insk_backend.domain.ArticleAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleAnalysisRepository extends JpaRepository<ArticleAnalysis, Long> {
}
