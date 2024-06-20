package com.losevskiyfz.springjwttutorial.controller;

import com.losevskiyfz.springjwttutorial.dto.RegisterUserDto;
import com.losevskiyfz.springjwttutorial.mapper.RegisterUserToUserImpl;
import com.losevskiyfz.springjwttutorial.model.User;
import com.losevskiyfz.springjwttutorial.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthorizedController {

    private final UserRepository userRepository;
    private final RegisterUserToUserImpl registerUserToUser;

    @PostMapping("/user/register")
    public User registerUser(@RequestBody RegisterUserDto registerUserDTO) {
        User user = registerUserToUser.toUser(registerUserDTO);
        return userRepository.save(user);
    }

    @GetMapping("/me")
    @PostAuthorize("returnObject == authentication.name")
    public String me(Principal principal) {
        return principal.getName();
    }

}
