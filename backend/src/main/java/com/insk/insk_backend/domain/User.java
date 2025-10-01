package com.insk.insk_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // JPA Auditing 기능을 사용하기 위해 추가
@Entity // 이 클래스가 데이터베이스 테이블과 매핑되는 JPA 엔티티임을 선언
@Getter // Lombok을 사용하여 모든 필드에 대한 getter 메소드를 자동으로 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 기본 생성자를 통한 객체 생성을 막음 (JPA 스펙상 필요)
@Table(name = "users") // 실제 데이터베이스의 'users' 테이블과 매핑
public class User {

    @Id // 이 필드가 테이블의 Primary Key(기본 키)임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스(MySQL의 AUTO_INCREMENT)에 위임
    @Column(name = "user_id", updatable = false) // 'user_id' 컬럼과 매핑, 업데이트 불가
    private Long id;

    @Column(name = "email", nullable = false, unique = true) // 'email' 컬럼과 매핑, null 불가, 유니크 제약 조건
    private String email;

    @Column(name = "password", nullable = false) // 'password' 컬럼과 매핑, null 불가
    private String password;

    @CreatedDate // 엔티티가 생성될 때의 시간이 자동으로 저장됨
    @Column(name = "created_at", nullable = false, updatable = false) // 'created_at' 컬럼과 매핑, null 불가, 업데이트 불가
    private LocalDateTime createdAt;

    @Builder // 빌더 패턴을 사용하여 객체를 생성할 수 있도록 함
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

