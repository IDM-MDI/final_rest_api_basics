package com.epam.esm.security.jwt;

import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.JwtUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = WebApplication.class)
@ExtendWith(MockitoExtension.class)
@PrepareForTest({JwtTokenProvider.class,JwtTokenProviderTest.class})
class JwtTokenProviderTest {

    @MockBean
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider provider;

    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZXMiOlsidXNlciJdLCJpYXQiOjE2NTg3NDk0MDcsImV4cCI6MTY1ODc1MzAwN30.Z3SNyRnNtsPBJebBsjZNVF2ONGlC7TSj012axQLFQnLXfzKKJTEhLtwPHhP0lH1J3wXobpNIYPGrIAz2fgA1wg";

    private final UserDto user = new UserDto(
            1L,
            "test",
            "password",
            null,
            ACTIVE.name(),
            List.of(new RoleDto(1L, "user")),
            null
    );

    @Test
    void getAuthentication() {
        JwtUser jwtUser = JwtUserFactory.create(user);
        Authentication expected = new UsernamePasswordAuthenticationToken(jwtUser,null,jwtUser.getAuthorities());
        String actualToken = provider.createToken(user);
        when(userDetailsService.loadUserByUsername(provider.getUsername(actualToken)))
                .thenReturn(jwtUser);
        Authentication actual = provider.getAuthentication(actualToken);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void getUsername() {
        String token = provider.createToken(user);
        String actualUserName = provider.getUsername(token);
        Assertions.assertEquals(user.getUsername(),actualUserName);
    }

    @Test
    void resolveToken() {
        request.addHeader("Authorization", "Bearer " + token);
        String actualToken = provider.resolveToken(request);
        Assertions.assertEquals(token,actualToken);
    }

    @Test
    void validateToken() {
        Assertions.assertTrue(provider.validateToken(provider.createToken(user)));
    }
}