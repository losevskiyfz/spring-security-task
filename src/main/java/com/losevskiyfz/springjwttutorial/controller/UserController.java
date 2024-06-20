package com.losevskiyfz.springjwttutorial.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping("/test/user-only-access")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String isAuthorized() {
        return "Авторизован";
    }

}
