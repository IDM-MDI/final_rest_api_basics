package com.epam.esm.validator;

import com.epam.esm.config.SecurityConfig;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.JwtUserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static com.epam.esm.validator.JwtValidator.isAuthenticationNull;
import static com.epam.esm.validator.JwtValidator.isJwtUserValid;
import static com.epam.esm.validator.JwtValidator.isUserNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {WebApplication.class, SecurityConfig.class})
@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test","prod"})
class JwtValidatorTest {
    private final UserDto user = new UserDto(
            1L,
            "usernameTest",
            "passwordTest",
            null,
            ACTIVE.name(),
            List.of(new RoleDto(1L,"ADMIN"),new RoleDto(2L,"USER")),
            null);

    @Test
    void isJwtUserValidTestShouldTrue() {
        JwtUser jwtUser = JwtUserFactory.create(user);
        SecurityContextImpl context = new SecurityContextImpl();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwtUser, user.getPassword());
        auth.setDetails(jwtUser);

        context.setAuthentication(auth);

        try(MockedStatic<SecurityContextHolder> securityContext = mockStatic(SecurityContextHolder.class)) {
            when(SecurityContextHolder.getContext()).thenReturn(context);
            Assertions.assertTrue(isJwtUserValid(user.getUsername()));
        }
    }

    @Test
    void isJwtUserValidTestShouldFalseBecauseAuthIsNull() {
        SecurityContextImpl context = new SecurityContextImpl();

        try(MockedStatic<SecurityContextHolder> securityContext = mockStatic(SecurityContextHolder.class)) {
            when(SecurityContextHolder.getContext()).thenReturn(context);
            Assertions.assertFalse(isJwtUserValid(user.getUsername()));
        }
    }

    @Test
    void isJwtUserValidTestShouldFalseBecauseUserIsNull() {
        SecurityContextImpl context = new SecurityContextImpl(new UsernamePasswordAuthenticationToken(null,null));

        try(MockedStatic<SecurityContextHolder> securityContext = mockStatic(SecurityContextHolder.class)) {
            when(SecurityContextHolder.getContext()).thenReturn(context);
            Assertions.assertFalse(isJwtUserValid(user.getUsername()));
        }
    }

    @Test
    void isAuthenticationNullTestShouldTrue() {
        SecurityContextImpl context = new SecurityContextImpl();

        try(MockedStatic<SecurityContextHolder> securityContext = mockStatic(SecurityContextHolder.class)) {
            when(SecurityContextHolder.getContext()).thenReturn(context);
            Assertions.assertTrue(isAuthenticationNull());
        }
    }

    @Test
    void isAuthenticationNullTestShouldFalse() {
        SecurityContextImpl context = new SecurityContextImpl(new UsernamePasswordAuthenticationToken(null,null));

        try(MockedStatic<SecurityContextHolder> securityContext = mockStatic(SecurityContextHolder.class)) {
            when(SecurityContextHolder.getContext()).thenReturn(context);
            Assertions.assertFalse(isAuthenticationNull());
        }
    }

    @Test
    void isUserNullTestShouldFalse() {
        JwtUser jwtUser = JwtUserFactory.create(user);
        Assertions.assertFalse(isUserNull(jwtUser));
    }

    @Test
    void isUserNullTestShouldTrueByUsername() {
        JwtUser jwtUser = JwtUserFactory.create(new UserDto());
        Assertions.assertTrue(isUserNull(jwtUser));
    }
    @Test
    void isUserNullTestShouldTrueByNull() {
        Assertions.assertTrue(isUserNull(null));
    }
}