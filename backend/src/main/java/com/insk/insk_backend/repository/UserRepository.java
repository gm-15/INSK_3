package com.insk.insk_backend.repository;

import com.insk.insk_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 이 인터페이스가 Spring Data JPA 리포지토리임을 선언
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository<User, Long>를 상속받는 것만으로
    // save(), findById(), findAll(), delete() 등
    // 기본적인 CRUD 메소드가 자동으로 구현됩니다.
}