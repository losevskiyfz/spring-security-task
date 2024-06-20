package com.losevskiyfz.springjwttutorial.dto;

import com.losevskiyfz.springjwttutorial.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserDto {
    private String login;
    private String password;
    private Role role;
}