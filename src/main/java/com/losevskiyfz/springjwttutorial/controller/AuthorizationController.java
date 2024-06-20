package com.losevskiyfz.springjwttutorial.controller;

import com.losevskiyfz.springjwttutorial.dto.AuthRequestDto;
import com.losevskiyfz.springjwttutorial.dto.JwtResponseDto;
import com.losevskiyfz.springjwttutorial.dto.RefreshTokenRequestDto;
import com.losevskiyfz.springjwttutorial.model.RefreshToken;
import com.losevskiyfz.springjwttutorial.service.JwtService;
import com.losevskiyfz.springjwttutorial.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/auth")
    public JwtResponseDto authenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken
                                (
                                        authRequestDto.username(),
                                        authRequestDto.password()
                                )
                );

        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.username());
            return new JwtResponseDto(jwtService.generateToken(authRequestDto.username()), refreshToken.token());
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }

    @PostMapping("/auth/refresh")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO) {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.token())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::user)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getLogin());
                    return new JwtResponseDto(accessToken, refreshTokenRequestDTO.token());
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
    }

}
