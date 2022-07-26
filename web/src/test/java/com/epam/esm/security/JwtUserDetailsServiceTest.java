package com.epam.esm.security;

import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.StatusDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.JwtUserFactory;
import com.epam.esm.service.impl.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = WebApplication.class)
@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @MockBean
    private UserService service;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    private final UserDto user = new UserDto(
            1L,
            "test",
            "password",
            null,
            new StatusDto(1L,"ACTIVE"),
            List.of(new RoleDto(1L, "user")),
            null
    );

    private final JwtUser jwtUser = JwtUserFactory.create(user);

    @Test
    void loadUserByUsername() {
        when(service.findUserByUsername(user.getUsername()))
                .thenReturn(user);
        UserDetails actual = userDetailsService.loadUserByUsername(user.getUsername());
        Assertions.assertEquals(jwtUser,actual);
    }
}