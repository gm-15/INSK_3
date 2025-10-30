package com.insk.insk_backend.controller;

import com.insk.insk_backend.dto.ArticleDto;
import com.insk.insk_backend.service.ArticleService;
import com.insk.insk_backend.service.NewsPipelineService; // 1. NewsPipelineService 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final NewsPipelineService newsPipelineService; // 2. NewsPipelineService 주입

    /**
     * 분석된 뉴스 기사 목록을 조회하는 API (페이징 및 필터링)
     * (3주차 테스트 워크플로우 5단계에서 사용)
     */
    @GetMapping
    public ResponseEntity<Page<ArticleDto.Response>> getArticles(
            @RequestParam(required = false) String category,
            @PageableDefault(size = 10, sort = "publishedAt") Pageable pageable) {

        Page<ArticleDto.Response> articles = articleService.getArticles(category, pageable);
        return ResponseEntity.ok(articles);
    }

    /**
     * 수동으로 뉴스 파이프라인을 실행하는 API (테스트용)
     */
    @PostMapping("/run-pipeline")
    public ResponseEntity<String> runPipeline() {

        // [ ⭐️⭐️⭐️ 이 부분 수정 ⭐️⭐️⭐️ ]
        // newsPipelineService.runPipelineOnStart(); -> newsPipelineService.runPipeline();
        newsPipelineService.runPipeline();

        return ResponseEntity.ok("뉴스 파이프라인 실행을 시작합니다.");
    }

    /**
     * (참고) API 명세서에 따른 특정 뉴스 상세 조회 API
     * Dto 및 Service 구현이 필요할 수 있습니다.
     */
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDto.DetailResponse> getArticleById(@PathVariable Long articleId) {
        ArticleDto.DetailResponse article = articleService.getArticleById(articleId);
        return ResponseEntity.ok(article);
    }
}