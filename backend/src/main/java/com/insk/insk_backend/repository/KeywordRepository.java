package com.insk.insk_backend.repository;

import com.insk.insk_backend.domain.Keyword;
import com.insk.insk_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    // 특정 사용자가 생성한 모든 키워드를 찾는 메소드
    List<Keyword> findByUser(User user);
}
