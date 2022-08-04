package com.epam.esm.security;

import com.epam.esm.dto.UserDto;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.JwtUserFactory;
import com.epam.esm.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {
    private final UserServiceImpl service;

    @Autowired
    public JwtUserDetailsService(UserServiceImpl service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = service.findUserByUsername(username);
        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("user with username - {} successfully loaded", username);
        return jwtUser;
    }
}
