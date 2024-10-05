package com.poten.dive_in.auth.repository;

import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.entity.TokenManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenManagerRepository extends JpaRepository<TokenManager, Long> {
    boolean existsByRefreshToken(String refreshToken);
    int deleteByRefreshToken(String token);
    int deleteByMemberId(Long id);
}
