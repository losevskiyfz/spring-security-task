package com.losevskiyfz.springjwttutorial.mapper;

import com.losevskiyfz.springjwttutorial.dto.RegisterUserDto;
import com.losevskiyfz.springjwttutorial.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RegisterUserToUserImpl implements RegisterUserToUser {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User toUser(RegisterUserDto registerUserDTO) {
        if ( registerUserDTO == null ) {
            return null;
        }

        User user = new User();

        user.setLogin( registerUserDTO.getLogin() );
        user.setRole( registerUserDTO.getRole() );

        user.setPassword( passwordEncoder.encode(registerUserDTO.getPassword()) );

        return user;
    }
}