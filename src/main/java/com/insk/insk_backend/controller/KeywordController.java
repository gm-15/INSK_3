package com.insk.insk_backend.controller;

import com.insk.insk_backend.dto.KeywordDto;
import com.insk.insk_backend.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/keywords")
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping
    public ResponseEntity<KeywordDto.Response> createKeyword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody KeywordDto.CreateRequest requestDto) {
        String userEmail = userDetails.getUsername();
        KeywordDto.Response response = keywordService.createKeyword(userEmail, requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<KeywordDto.Response>> getKeywords(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        List<KeywordDto.Response> keywords = keywordService.findKeywordsByUser(userEmail);
        return ResponseEntity.ok(keywords);
    }

    @DeleteMapping("/{keywordId}")
    public ResponseEntity<Void> deleteKeyword(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long keywordId) {
        String userEmail = userDetails.getUsername();
        keywordService.deleteKeyword(userEmail, keywordId);
        return ResponseEntity.noContent().build();
    }
}
