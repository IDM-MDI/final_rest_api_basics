package com.epam.esm.validator;

import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.UserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    @Test
    void isAuthorizationValid() {
        UserDto user = new UserDto();
        user.setUsername("login");
        user.setPassword("password");
        AuthenticationDto authentication = new AuthenticationDto("login","password");
        assertTrue(UserValidator.isAuthorizationValid(user,authentication));
    }
}