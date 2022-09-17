package com.epam.esm.security;

import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.JwtUserFactory;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = WebApplication.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test","prod"})
class JwtUserDetailsServiceTest {

    @MockBean
    private UserServiceImpl service;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    private final UserDto user = new UserDto(
            1L,
            "test",
            "password",
            null,
            ACTIVE.name(),
            List.of(new RoleDto(1L, "user")),
            null
    );

    private final JwtUser jwtUser = JwtUserFactory.create(user);

    @Test
    void loadUserByUsername() {
        when(service.findUserDtoByUsername(user.getUsername()))
                .thenReturn(user);
        UserDetails actual = userDetailsService.loadUserByUsername(user.getUsername());
        Assertions.assertEquals(jwtUser,actual);
    }
}