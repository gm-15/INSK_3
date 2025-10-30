package com.insk.insk_backend.service;

import com.insk.insk_backend.domain.User;
import com.insk.insk_backend.dto.UserDto;
import com.insk.insk_backend.jwt.JwtTokenProvider;
import com.insk.insk_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j // 로깅을 위한 Lombok 어노테이션 추가
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto.SignUpResponse signup(UserDto.SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        User savedUser = userRepository.save(newUser);

        return new UserDto.SignUpResponse(savedUser.getId(), savedUser.getEmail());
    }

    private final JwtTokenProvider jwtTokenProvider;

    public UserDto.LoginResponse login(UserDto.LoginRequest requestDto) {
        try { // <-- try 추가
            User user = userRepository.findByEmail(requestDto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

            if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }

            String token = jwtTokenProvider.createToken(user.getEmail());
            return new UserDto.LoginResponse(token);
        } catch (Exception e) { // <-- catch 추가
            log.error("로그인 처리 중 예외 발생!", e); // <-- 예외 로그 남기기
            throw e; // 원래 예외를 다시 던져서 흐름 유지
        }
    }
}



