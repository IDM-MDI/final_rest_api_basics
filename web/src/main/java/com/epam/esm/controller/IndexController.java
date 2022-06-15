package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.UserHateoas;
import com.epam.esm.security.oauth.CustomOAuth2UserService;
import com.epam.esm.service.LoginService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/")
@Profile("prod")
public class IndexController {

    private final LoginService loginService;
    private final UserHateoas hateoas;
    private final OAuth2AuthorizedClientService clientService;
    @Autowired
    public IndexController(LoginService loginService, UserHateoas hateoas, OAuth2AuthorizedClientService oAuth2UserService) {
        this.loginService = loginService;
        this.hateoas = hateoas;
        this.clientService = oAuth2UserService;
    }

    @GetMapping
    public String empty() {
        return "";
    }

    @PostMapping("/login")
    public DtoPage<UserDto> login(@RequestBody AuthenticationDto dto) throws ServiceException, RepositoryException {
        DtoPage<UserDto> page = loginService.authenticate(dto);
        hateoas.setUserHateoas(page);
        return page;
    }

    @GetMapping("/login/oauth2/code/github")
    public DtoPage<UserDto> login(@RequestParam Map<String,String> params) {
        return new DtoPage<>();
    }
}
