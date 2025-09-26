-- 기존 테이블이 존재할 경우 삭제하여 초기화합니다.
DROP TABLE IF EXISTS `article_analyses`;
DROP TABLE IF EXISTS `keywords`;
DROP TABLE IF EXISTS `articles`;
DROP TABLE IF EXISTS `users`;


-- 사용자 정보를 저장하는 테이블
CREATE TABLE `users` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 수집된 뉴스 원본 정보를 저장하는 테이블
CREATE TABLE `articles` (
    `article_id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) NOT NULL,
    `original_url` VARCHAR(512) NOT NULL UNIQUE,
    `published_at` DATETIME NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 사용자가 등록한 관심 키워드를 저장하는 테이블
CREATE TABLE `keywords` (
    `keyword_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `keyword` VARCHAR(100) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`keyword_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- AI가 뉴스를 분석한 결과를 저장하는 테이블
CREATE TABLE `article_analyses` (
    `analysis_id` BIGINT NOT NULL AUTO_INCREMENT,
    `article_id` BIGINT NOT NULL,
    `summary` TEXT NOT NULL,
    `insight` TEXT,
    `category` VARCHAR(50),
    `tags` VARCHAR(255),
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`analysis_id`),
    FOREIGN KEY (`article_id`) REFERENCES `articles` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
