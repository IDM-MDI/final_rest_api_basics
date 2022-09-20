package com.epam.esm.service;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.config.SecurityConfig;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.WebException;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.security.jwt.JwtUserFactory;
import com.epam.esm.service.page.PageUserService;
import com.epam.esm.util.HashGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {WebApplication.class, SecurityConfig.class})
@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test","prod"})
class LoginServiceTest {
    private final MockHttpServletRequest request = new MockHttpServletRequest();

    @MockBean
    private JwtTokenProvider provider;
    @MockBean
    private AuthenticationManager manager;
    @MockBean
    private PageUserService service;

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

    @Test
    void authenticate() {
        AuthenticationDto authDto = new AuthenticationDto(user.getUsername(),user.getPassword());
        DtoPage<UserDto> expected = new DtoPageBuilder<UserDto>()
                .setContent(List.of(user)).build();
        when(service.login(authDto)).thenReturn(expected);
        when(manager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), HashGenerator.generateBySHA(user.getPassword())))).thenReturn(null);
        createToken();
        DtoPage<UserDto> actual = loginService.authenticate(authDto);
        expected.getContent().get(0).setJwt(token);
        Assertions.assertEquals(expected,actual);
    }

    @SneakyThrows
    @Test
    void getUsernameByContext() {
        String expected = "test";
        SecurityContextImpl context = new SecurityContextImpl(new UsernamePasswordAuthenticationToken(JwtUserFactory.create(user),null));
        try(MockedStatic<SecurityContextHolder> securityContext = mockStatic(SecurityContextHolder.class)) {
            when(SecurityContextHolder.getContext()).thenReturn(context);
            String actual = loginService.getUsernameByContext();
            Assertions.assertEquals(expected,actual);
        }
    }
    @SneakyThrows
    @Test
    void getUsernameByContextShouldTrowWebException() {
        Assertions.assertThrows(WebException.class,() -> loginService.getUsernameByContext());
    }
}