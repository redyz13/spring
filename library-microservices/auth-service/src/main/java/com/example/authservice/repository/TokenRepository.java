package com.example.authservice.repository;

import com.example.authservice.domain.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {
    @Transactional
    @Modifying
    @Query("update TokenEntity t set t.revoked = ?1 where t.id = ?2")
    void updateRevokedById(boolean revoked, UUID id);
}
