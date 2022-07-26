package com.epam.esm.service;

import com.epam.esm.config.SecurityConfig;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.impl.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {WebApplication.class, SecurityConfig.class})
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    private final MockHttpServletRequest request = new MockHttpServletRequest();

    @MockBean
    private JwtTokenProvider provider;

    @Autowired
    private LoginService loginService;

    private final UserDto user = new UserDto(
            1L,
            "test",
            "password",
            null,
            null,
            List.of(new RoleDto(1L, "user")),
            null
    );
    private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZXMiOlsidXNlciJdLCJpYXQiOjE2NTg2ODgyOTUsImV4cCI6MTY1ODY5MTg5NX0.utFpqbPKSJrNFtZeuRovzNa3IW3yJ-BzWyXKRPzEZhz6TrAPUxjhMwzVqOmWTtg2dWaQ74w4pksaIFwUIZ8ERw";

    @Test
    void getToken() {
        when(provider.resolveToken(request))
                .thenReturn(token);
        String actual = loginService.getToken(request);
        Assertions.assertEquals(token,actual);
    }

    @Test
    void getUsername() {
        getToken();
        when(provider.getUsername(token))
                .thenReturn(user.getUsername());
        String actual = loginService.getUsername(request);
        Assertions.assertEquals(user.getUsername(),actual);
    }

    @Test
    void createToken() {
        when(provider.createToken(user))
                .thenReturn(token);
        String actual = loginService.createToken(user);
        Assertions.assertEquals(token,actual);
    }
}