package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
@Profile("prod")
public class IndexController {

    private final LoginService loginService;

    @Autowired
    public IndexController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String empty() {
        return "";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationDto dto) {
        return loginService.authenticate(dto);
    }
}
