package com.losevskiyfz.springjwttutorial.service;

import com.losevskiyfz.springjwttutorial.model.RefreshToken;
import com.losevskiyfz.springjwttutorial.repository.RefreshTokenRepository;
import com.losevskiyfz.springjwttutorial.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByLogin(username)
                        .orElseThrow(()-> new NoSuchElementException("User not found"))
                )
                .token(UUID.randomUUID().toString())
                .expiryTimestamp(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }


    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryTimestamp().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
