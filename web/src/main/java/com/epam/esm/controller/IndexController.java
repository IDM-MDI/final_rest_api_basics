package com.epam.esm.controller;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.UserHateoas;
import com.epam.esm.service.LoginService;
import com.epam.esm.validator.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = "/api/v1")
@Profile("prod")
public class IndexController {

    private final LoginService loginService;
    private final UserHateoas hateoas;

    @Autowired
    public IndexController(LoginService loginService, UserHateoas hateoas) {
        this.loginService = loginService;
        this.hateoas = hateoas;
    }

    @GetMapping("/login/oauth/success")
    public DtoPage<String> successLogin(@RequestParam String token) {
        return new DtoPageBuilder<String>()
                .setContent(List.of("Your Bearer token is: " + token))
                .build();
    }

    @PostMapping("/login")
    public DtoPage<UserDto> login(@RequestBody AuthenticationDto dto) throws ServiceException, RepositoryException {
        DtoPage<UserDto> page = loginService.authenticate(dto);
        hateoas.setHateoas(page);
        return page;
    }

    @GetMapping("/jwt")
    public boolean jwtUserGuard(@RequestParam String username) {
        return JwtValidator.isJwtUserValid(username);
    }
}
