package com.insk.insk_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Naver News API 응답을 매핑하기 위한 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 응답의 모든 필드를 매핑하지 않으므로, 모르는 필드는 무시
public class NaverNewsDto {

    private String title;
    private String originallink; // Naver API는 'originalUrl'이 아닌 'originallink'로 응답
    private String link;
    private String description;
    private String pubDate;

    // NewsPipelineService에서 사용할 필드 getter

    public String getOriginalUrl() {
        return this.originallink;
    }

    // Naver API의 날짜 형식(RFC 1123)을 LocalDateTime으로 변환
    public LocalDateTime getPubDate() {
        if (this.pubDate == null) {
            return LocalDateTime.now();
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            return LocalDateTime.parse(this.pubDate, formatter);
        } catch (Exception e) {
            return LocalDateTime.now(); // 파싱 실패 시 현재 시간
        }
    }

    /**
     * Naver API의 최상위 응답 구조 (items 리스트를 포함)
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchResponse {
        private List<NaverNewsDto> items;
    }
}