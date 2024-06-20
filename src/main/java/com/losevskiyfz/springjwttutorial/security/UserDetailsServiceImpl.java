package com.losevskiyfz.springjwttutorial.security;

import com.losevskiyfz.springjwttutorial.model.User;
import com.losevskiyfz.springjwttutorial.repository.UserRepository;
import com.losevskiyfz.springjwttutorial.security.config.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.debug("Entering in loadUserByUsername Method...");
        Optional<User> userOptional = userRepository.findByLogin(username);
        User user = userOptional.orElseThrow(() -> {
            logger.error("Username not found: {}", username);
            return new UsernameNotFoundException(username);
        });

        logger.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user);
    }
}