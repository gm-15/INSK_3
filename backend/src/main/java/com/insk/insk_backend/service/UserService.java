package com.insk.insk_backend.service;

import com.insk.insk_backend.domain.User;
import com.insk.insk_backend.dto.UserDto;
import com.insk.insk_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserDto.SignupResponse signup(UserDto.SignupRequest request) {
        // TODO: 이메일 중복 검사 로직 추가
        // TODO: 비밀번호 암호화 로직 추가

        User newUser = User.builder()
                .email(request.getEmail())
                .password(request.getPassword()) // 임시로 평문 저장
                .build();

        User savedUser = userRepository.save(newUser);

        return new UserDto.SignupResponse(savedUser.getId(), savedUser.getEmail());
    }
}
