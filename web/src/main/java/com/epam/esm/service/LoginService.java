package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.WebException;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.service.page.PageUserService;
import com.epam.esm.util.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginService {
    private final AuthenticationManager manager;
    private final JwtTokenProvider provider;
    private final PageUserService service;

    @Autowired
    public LoginService(AuthenticationManager manager, JwtTokenProvider provider, PageUserService userServiceImpl) {
        this.manager = manager;
        this.provider = provider;
        this.service = userServiceImpl;
    }

    public DtoPage<UserDto> authenticate(AuthenticationDto authentication) {
        authenticateByManager(authentication);
        return getDtoPage(authentication);
    }

    public String getToken(HttpServletRequest request) {
        return provider.resolveToken(request);
    }

    public String getUsernameByContext() throws WebException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            JwtUser user = (JwtUser) authentication.getPrincipal();
            return user.getUsername();
        } catch (NullPointerException e) {
            throw new WebException("User not found");
        }
    }

    public String getUsername(HttpServletRequest request) {
        return provider.getUsername(getToken(request));
    }
    public String createToken(UserDto dto) {
        return provider.createToken(dto);
    }
    private void authenticateByManager(AuthenticationDto authentication) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(authentication.getUsername(),
                HashGenerator.generateBySHA(authentication.getPassword())));
    }
    private DtoPage<UserDto> getDtoPage(AuthenticationDto authentication) {
        DtoPage<UserDto> page = service.login(authentication);
        UserDto user = page.getContent().get(0);
        user.setJwt(createToken(user));
        return page;
    }
}
