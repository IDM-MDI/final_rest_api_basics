package com.epam.esm.validator;

import com.epam.esm.security.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtValidator {
    private JwtValidator() {}
    public static boolean isJwtUserValid(String username) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(isAuthenticationNull() || isUserNull((JwtUser)authentication.getPrincipal())) {
            return false;
        }
        return ((JwtUser) authentication.getPrincipal()).getUsername().equals(username);
    }

    public static boolean isAuthenticationNull() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    public static boolean isUserNull(JwtUser user) {
        return user == null ||
                user.getUsername() == null ||
                user.getUsername().isBlank();
    }
}
