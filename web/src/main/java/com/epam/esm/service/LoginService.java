package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.impl.UserService;
import com.epam.esm.util.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public DtoPage<UserDto> authenticate(AuthenticationDto dto) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),
                            HashGenerator.generateHash(dto.getPassword())));
        DtoPage<UserDto> page = service.loginWithDtoPage(dto);
        UserDto user = page.getContent().stream().findFirst().get();
        user.setJwt(provider.createToken(user));
        return page;
    }
}
