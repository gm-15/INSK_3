package com.insk.insk_backend.dto;

import com.insk.insk_backend.domain.Keyword;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class KeywordDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRequest {
        private String keyword;

        public CreateRequest(String keyword) {
            this.keyword = keyword;
        }
    }

    @Getter
    public static class Response {
        private final Long keywordId;
        private final String keyword;
        private final LocalDateTime createdAt;

        public Response(Keyword keyword) {
            this.keywordId = keyword.getId();
            this.keyword = keyword.getKeyword();
            this.createdAt = keyword.getCreatedAt();
        }
    }
}
