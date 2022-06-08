package com.epam.esm.security;

import com.epam.esm.entity.User;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.JwtUserFactory;
import com.epam.esm.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService service;

    @Autowired
    public JwtUserDetailsService(UserService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = service.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username - " + username + " not found"));
        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("user with username - {} successfully loaded", username);
        return jwtUser;
    }
}
