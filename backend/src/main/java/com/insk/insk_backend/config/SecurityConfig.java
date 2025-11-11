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
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// ✅ CORS 관련 import
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                // ✅ CORS 활성화 (아래 corsConfigurationSource() Bean과 연동)
                .cors(Customizer.withDefaults())

                // 1. CSRF 보호 비활성화 (JWT 토큰 사용 시 불필요)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. HTTP Basic 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // 3. Form 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)

                // 4. 세션 정책: STATELESS (세션을 사용하지 않음)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5. 경로별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth

                        // 로그인 API 및 기본 /error 경로는 모두 허용
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
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ 전역 CORS 설정 Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 프론트엔드 Origin 목록
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000" // 로컬 개발 환경
                // 나중에 배포된 프론트 주소도 여기 추가하면 됨
                // 예: "https://insk-frontend.vercel.app"
        ));

        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 허용할 헤더
        configuration.setAllowedHeaders(List.of("*"));

        // 인증 정보(쿠키, Authorization 헤더 등) 허용
        configuration.setAllowCredentials(true);

        // 모든 API 경로에 위 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
