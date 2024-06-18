package com.losevskiyfz.springjwttutorial.mapper;

import com.losevskiyfz.springjwttutorial.dto.RegisterUserDTO;
import com.losevskiyfz.springjwttutorial.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class RegisterUserToUser {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(registerUserDTO.getPassword()))")
    public abstract User toUser(RegisterUserDTO registerUserDTO);

}
