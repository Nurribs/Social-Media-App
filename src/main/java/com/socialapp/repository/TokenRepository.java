package com.socialapp.repository;

import com.socialapp.model.entity.Token;
import com.socialapp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.time.Instant;

public interface TokenRepository extends JpaRepository<Token, Long>{
    Optional<Token> findByToken(String token);
    long deleteByUser(User user);

    void deleteAllByUserId(Long userId);
    long deleteByExpiresAtBefore(Instant time);
}

