package com.insk.insk_backend.client;

import com.insk.insk_backend.dto.NaverNewsDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class NaverNewsClient {

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    private final String API_URL = "https://openapi.naver.com/v1/search/news.json";
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Naver News API를 호출하여 뉴스 목록을 가져옵니다.
     */
    public List<NaverNewsDto> searchNews(String query, int display) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = API_URL + "?query=" + query + "&display=" + display + "&sort=sim";

        try {
            ResponseEntity<NaverNewsDto.SearchResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, NaverNewsDto.SearchResponse.class
            );
            return response.getBody() != null ? response.getBody().getItems() : Collections.emptyList();
        } catch (Exception e) {
            log.error("Naver News API 호출 실패: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * [ ⭐️ 'scrapeArticleBody'의 실제 구현 ⭐️ ]
     * Jsoup을 사용하여 기사 원문의 본문을 스크래핑합니다.
     */
    public String scrapeArticleBody(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            // (참고) 이 셀렉터는 언론사마다 다를 수 있습니다. 지금은 일반적인 경우를 가정합니다.
            return doc.select("article, #articleBody, #article_body, #newsct_article").text();
        } catch (Exception e) {
            log.warn("Jsoup 스크래핑 실패 (URL: {}): {}", url, e.getMessage());
            return null;
        }
    }
}