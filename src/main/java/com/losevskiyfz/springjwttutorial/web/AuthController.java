package com.losevskiyfz.springjwttutorial.web;

import com.losevskiyfz.springjwttutorial.dto.AuthRequestDTO;
import com.losevskiyfz.springjwttutorial.dto.JwtResponseDTO;
import com.losevskiyfz.springjwttutorial.dto.RefreshTokenRequestDTO;
import com.losevskiyfz.springjwttutorial.dto.RegisterUserDTO;
import com.losevskiyfz.springjwttutorial.mapper.RegisterUserToUserImpl;
import com.losevskiyfz.springjwttutorial.model.RefreshToken;
import com.losevskiyfz.springjwttutorial.model.User;
import com.losevskiyfz.springjwttutorial.repository.UserRepository;
import com.losevskiyfz.springjwttutorial.service.JwtService;
import com.losevskiyfz.springjwttutorial.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final RegisterUserToUserImpl registerUserToUserImpl;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository,
                          RefreshTokenService refreshTokenService,
                          RegisterUserToUserImpl registerUserToUserImpl
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.registerUserToUserImpl = registerUserToUserImpl;
    }

    @PostMapping("/auth")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken
                                (
                                        authRequestDTO.getUsername(),
                                        authRequestDTO.getPassword()
                                )
                );

        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            return JwtResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                    .token(refreshToken.getToken())
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }

    @PostMapping("/auth/refresh")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getLogin());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
    }

    @PostMapping("/user/register")
    public User registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        User user = registerUserToUserImpl.toUser(registerUserDTO);
        return userRepository.save(user);
    }

    @GetMapping("/user")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/test/user-only-access")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String isAuthorized() {
        return "Авторизован";
    }

    @GetMapping("/me")
    @PostAuthorize("returnObject == authentication.name")
    public String me(Principal principal) {
        return principal.getName();
    }

}
