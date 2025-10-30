package com.insk.insk_backend.config;

import com.insk.insk_backend.jwt.JwtAuthenticationFilter;
import com.insk.insk_backend.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    // PasswordEncoder를 Bean으로 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager를 Bean으로 등록합니다. (UserService에서 주입받아 사용)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 보호 비활성화 (JWT 토큰 사용 시 불필요)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. HTTP Basic 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // 3. Form 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)

                // 4. 세션 정책: STATELESS (세션을 사용하지 않음)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5. [⭐️⭐️⭐️ 핵심 수정 ⭐️⭐️⭐️] 경로별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // [수정] 로그인 API 및 기본 /error 경로는 모두 허용
                        .requestMatchers("/api/v1/auth/login", "/error").permitAll()

                        // 회원가입 API 모두 허용
                        .requestMatchers("/api/v1/auth/signup").permitAll()

                        // 기사 조회 API는 인증 없이 허용 (GET 요청만)
                        .requestMatchers(HttpMethod.GET, "/api/v1/articles", "/api/v1/articles/**").permitAll()

                        // 키워드 관련 API는 인증된 사용자만 접근 허용
                        .requestMatchers("/api/v1/keywords/**").authenticated()

                        // 그 외 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // 6. JWT 인증 필터 추가
                // Spring Security의 기본 로그인 필터(UsernamePasswordAuthenticationFilter) 전에
                // 우리가 만든 JwtAuthenticationFilter를 먼저 실행하도록 설정
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}