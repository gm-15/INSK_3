package com.insk.insk_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "article_analyses")
public class ArticleAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id", updatable = false)
    private Long id;

    // ArticleAnalysis(1) : Article(1) 관계. 분석 결과는 하나의 기사에 종속된다.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "summary", nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "insight", columnDefinition = "TEXT")
    private String insight;

    @Column(name = "category")
    private String category;

    @Column(name = "tags")
    private String tags;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ArticleAnalysis(Article article, String summary, String insight, String category, String tags) {
        this.article = article;
        this.summary = summary;
        this.insight = insight;
        this.category = category;
        this.tags = tags;
    }
}
