package com.epam.esm.security.oauth;

import com.epam.esm.controller.IndexController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.impl.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService service;
    private final JwtTokenProvider provider;

    @Autowired
    public OAuth2LoginSuccessHandler(UserService service, JwtTokenProvider provider) {
        this.service = service;
        this.provider = provider;
    }

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OidcUser principal = (OidcUser) authentication.getPrincipal();
        UserDto user = new UserDto();
        user.setUsername(principal.getAttribute("name"));
        user.setPassword(principal.getAttribute("at_hash"));
        user = service.oauth(user);
        user.setJwt(provider.createToken(user));
        log.info("User successfully authenticate");
        response.sendRedirect("/login/oauth/success?token=" + user.getJwt());
    }
}
