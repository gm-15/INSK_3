package com.insk.insk_backend.service;

import com.insk.insk_backend.domain.Keyword;
import com.insk.insk_backend.domain.User;
import com.insk.insk_backend.dto.KeywordDto;
import com.insk.insk_backend.repository.KeywordRepository;
import com.insk.insk_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    // 키워드 생성
    @Transactional
    public KeywordDto.Response createKeyword(String userEmail, KeywordDto.CreateRequest requestDto) {
        User user = findUserByEmail(userEmail);
        Keyword keyword = Keyword.builder()
                .user(user)
                .keyword(requestDto.getKeyword())
                .build();
        Keyword savedKeyword = keywordRepository.save(keyword);
        return new KeywordDto.Response(savedKeyword);
    }

    // 사용자의 모든 키워드 조회
    public List<KeywordDto.Response> findKeywordsByUser(String userEmail) {
        User user = findUserByEmail(userEmail);
        return keywordRepository.findByUser(user).stream()
                .map(KeywordDto.Response::new)
                .collect(Collectors.toList());
    }

    // 키워드 삭제
    @Transactional
    public void deleteKeyword(String userEmail, Long keywordId) {
        User user = findUserByEmail(userEmail);
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 키워드입니다."));

        // 현재 로그인한 사용자가 키워드의 주인인지 확인
        if (!keyword.getUser().equals(user)) {
            throw new IllegalStateException("삭제 권한이 없는 키워드입니다.");
        }
        keywordRepository.delete(keyword);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }
}
