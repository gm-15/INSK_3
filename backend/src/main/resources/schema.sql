-- 테이블이 존재하면 삭제 (개발 초기, 구조 변경 시 유용)
DROP TABLE IF EXISTS article_analyses;
DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS keywords;
DROP TABLE IF EXISTS users;

-- users 테이블 생성
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- keywords 테이블 생성
CREATE TABLE keywords (
    keyword_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    keyword VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE -- 사용자가 삭제되면 키워드도 삭제
);

-- articles 테이블 생성
CREATE TABLE articles (
    article_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    original_url VARCHAR(512) NOT NULL UNIQUE,
    published_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- article_analyses 테이블 생성
CREATE TABLE article_analyses (
    analysis_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id BIGINT NOT NULL UNIQUE, -- 1:1 관계
    summary TEXT NOT NULL,
    insight TEXT,
    category VARCHAR(50),
    tags VARCHAR(255), -- JSON 문자열 저장
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES articles(article_id) ON DELETE CASCADE -- 기사가 삭제되면 분석 결과도 삭제
);

-- 인덱스 추가 (성능 향상)
CREATE INDEX idx_keywords_user_id ON keywords(user_id);
CREATE INDEX idx_articles_published_at ON articles(published_at);
-- CREATE INDEX idx_articles_category ON articles(category); -- 카테고리 컬럼이 articles 테이블에 없으므로 주석 처리 (ArticleAnalysis 테이블에 있음)
CREATE INDEX idx_article_analyses_category ON article_analyses(category); -- ArticleAnalysis 테이블의 category 컬럼에 인덱스 추가
