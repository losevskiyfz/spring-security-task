package com.losevskiyfz.springjwttutorial.mapper;

import com.losevskiyfz.springjwttutorial.dto.RegisterUserDto;
import com.losevskiyfz.springjwttutorial.model.User;

public interface RegisterUserToUser {
    User toUser(RegisterUserDto registerUserDTO);
}
