package com.example.authservice.service.impl;

import com.example.authservice.repository.TokenRepository;
import com.example.authservice.service.TokenCleanupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupServiceImpl implements TokenCleanupService {
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    @Scheduled(cron = "${token.cleanup.cron}")
    public void cleanupTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deleted = tokenRepository.deleteByRevokedIsTrueOrExpiryDateBefore(now);
        log.info("🧹 Deleted: {} tokens", deleted);
    }
}
