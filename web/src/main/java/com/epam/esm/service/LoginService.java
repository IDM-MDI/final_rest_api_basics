package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.entity.User;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LoginService {
    private final AuthenticationManager manager;
    private final JwtTokenProvider provider;
    private final UserService service;

    @Autowired
    public LoginService(AuthenticationManager manager, JwtTokenProvider provider, UserService userService) {
        this.manager = manager;
        this.provider = provider;
        this.service = userService;
    }

    public String authenticate(AuthenticationDto dto) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));
        User user = service.findUserByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(""));
        return provider.createToken(user);
    }
}
