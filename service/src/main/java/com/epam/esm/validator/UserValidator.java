package com.epam.esm.validator;

import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.UserDto;

public class UserValidator {
    private UserValidator(){}
    public static boolean isAuthorizationValid(UserDto user, AuthenticationDto dto) {
        return user.getUsername().equals(dto.getUsername()) &&
                user.getPassword().equals(dto.getPassword());
    }
}
