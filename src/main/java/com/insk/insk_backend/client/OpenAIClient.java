package com.insk.insk_backend.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insk.insk_backend.dto.OpenAIDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class OpenAIClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 3주차 기획서 기반의 고정 프롬프트
    private final String SYSTEM_PROMPT = """
            당신은 SKT 전략기획 담당자입니다.
            제공되는 뉴스 기사 본문을 분석하여, 반드시 아래의 JSON 형식으로만 답변해야 합니다.
            {
              "summary": "기사를 5줄 이내로 요약",
              "insight": "이 뉴스가 SKT에 미칠 영향과 기회 요인을 한 문장으로 분석",
              "categoryMajor": "['Telco', 'LLM', 'INFRA', 'AI Ecosystem'] 중 가장 적합한 하나",
              "tags": ["핵심 키워드 1", "핵심 키워드 2", "핵심 키워드 3"]
            }
            """;

    /**
     * [ ⭐️ 'analyzeArticle'의 실제 구현 ⭐️ ]
     * 기사 본문을 OpenAI API로 전송하고 분석 결과를 DTO로 파싱합니다.
     */
    public OpenAIDto.AnalysisResponse analyzeArticle(String articleBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        OpenAIDto.ChatRequest requestBody = new OpenAIDto.ChatRequest(
                "gpt-4o", // 또는 gpt-4-turbo
                SYSTEM_PROMPT,
                articleBody,
                true // JSON 모드 활성화
        );

        try {
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            String response = restTemplate.postForObject(API_URL, entity, String.class);

            // OpenAI의 응답 JSON에서 content 부분만 추출하여 DTO로 변환
            JsonNode root = objectMapper.readTree(response);
            String jsonContent = root.path("choices").path(0).path("message").path("content").asText();

            // content 문자열(JSON)을 DTO로 파싱
            return objectMapper.readValue(jsonContent, OpenAIDto.AnalysisResponse.class);

        } catch (Exception e) {
            log.error("OpenAI API 분석 실패: {}", e.getMessage());
            return null;
        }
    }
}