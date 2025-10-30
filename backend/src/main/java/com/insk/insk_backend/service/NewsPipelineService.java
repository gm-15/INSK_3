package com.insk.insk_backend.service;

import com.insk.insk_backend.client.NaverNewsClient;
import com.insk.insk_backend.client.OpenAIClient;
import com.insk.insk_backend.domain.Article;
import com.insk.insk_backend.domain.ArticleAnalysis;
import com.insk.insk_backend.domain.Keyword;
import com.insk.insk_backend.dto.NaverNewsDto;
import com.insk.insk_backend.dto.OpenAIDto;
import com.insk.insk_backend.repository.ArticleAnalysisRepository;
import com.insk.insk_backend.repository.ArticleRepository;
import com.insk.insk_backend.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsPipelineService {

    private final KeywordRepository keywordRepository;
    private final ArticleRepository articleRepository;
    private final ArticleAnalysisRepository articleAnalysisRepository;
    private final NaverNewsClient naverNewsClient;
    private final OpenAIClient openAIClient;

    /**
     * [ ⭐️ 수정 ⭐️ ]
     * 수동 실행 및 스케줄링을 위한 메인 파이프라인 메소드
     * (이전의 @PostConstruct 삭제, 메소드 이름 변경)
     */
    @Transactional
    @Scheduled(cron = "0 0 8 * * *") // 매일 아침 8시에 자동 실행 (3주차 목표)
    public void runPipeline() {
        log.info("뉴스 수집 및 분석 파이프라인을 시작합니다...");

        List<Keyword> keywords = keywordRepository.findAll();
        if (keywords.isEmpty()) {
            log.warn("등록된 키워드가 없어 파이프라인을 종료합니다.");
            return;
        }

        for (Keyword keyword : keywords) {
            String query = keyword.getKeyword();
            log.info("'{}' 키워드로 네이버 뉴스 API 호출...", query);
            List<NaverNewsDto> newsItems = naverNewsClient.searchNews(query, 10); // 테스트를 위해 10개만

            for (NaverNewsDto item : newsItems) {
                // 1. 중복 기사 확인
                if (articleRepository.existsByOriginalUrl(item.getOriginalUrl())) {
                    log.info("이미 수집된 기사입니다 (SKIP): {}", item.getTitle());
                    continue;
                }

                // 2. 본문 스크래핑
                String articleBody = naverNewsClient.scrapeArticleBody(item.getOriginalUrl());
                if (articleBody == null || articleBody.isEmpty()) {
                    log.warn("기사 본문 스크래핑 실패 (SKIP): {}", item.getTitle());
                    continue;
                }

                // 3. OpenAI 분석
                OpenAIDto.AnalysisResponse analysisResponse = openAIClient.analyzeArticle(articleBody);
                if (analysisResponse == null) {
                    log.error("OpenAI 분석 실패 (SKIP): {}", item.getTitle());
                    continue;
                }

                // 4. Article 엔티티 저장
                Article newArticle = Article.builder()
                        .title(item.getTitle())
                        .originalUrl(item.getOriginalUrl())
                        .publishedAt(item.getPubDate())
                        .createdAt(LocalDateTime.now())
                        // [⭐️⭐️⭐️ 수정 ⭐️⭐️⭐️]
                        // .category(analysisResponse.getCategoryMajor()) // 이 라인을 삭제합니다.
                        .build();
                articleRepository.save(newArticle);

                // 5. ArticleAnalysis 엔티티 저장
                ArticleAnalysis analysis = ArticleAnalysis.builder()
                        .article(newArticle) // Article 엔티티와 연관관계 매핑
                        .summary(analysisResponse.getSummary())
                        .insight(analysisResponse.getInsight())
                        .category(analysisResponse.getCategoryMajor()) // 대분류
                        .tags(analysisResponse.getTagsJson()) // 키워드 (JSON 문자열)
                        .createdAt(LocalDateTime.now())
                        .build();
                articleAnalysisRepository.save(analysis);

                log.info("신규 기사 수집 및 분석 완료: {}", newArticle.getTitle());
            }
        }
        log.info("뉴스 파이프라인 실행을 완료했습니다.");
    }
}