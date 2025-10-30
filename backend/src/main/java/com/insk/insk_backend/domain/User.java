package com.insk.insk_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // UserDetails import

import java.time.LocalDateTime;
import java.util.Collection; // Collection import
import java.util.Collections; // Collections import

@EntityListeners(AuditingEntityListener.class) // Auditing 기능 활성화
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 기본 생성자 접근 제한
@Table(name = "users") // 데이터베이스 테이블 이름 명시
public class User implements UserDetails { // UserDetails 인터페이스 구현

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @CreatedDate // 엔티티 생성 시 시간 자동 저장
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder // 빌더 패턴으로 객체 생성
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // --- UserDetails 인터페이스 구현 메소드 ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 여기서는 단순화를 위해 역할(Role) 없이 빈 리스트 반환
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return email; // 사용자 이름으로 이메일 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (여기서는 항상 true)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부 (여기서는 항상 true)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호) 만료 여부 (여기서는 항상 true)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 (여기서는 항상 true)
    }
}
