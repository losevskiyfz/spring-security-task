package com.losevskiyfz.springjwttutorial.controller;

import com.losevskiyfz.springjwttutorial.model.User;
import com.losevskiyfz.springjwttutorial.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/user")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
