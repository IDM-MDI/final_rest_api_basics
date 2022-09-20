package com.epam.esm.security.oauth;

import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = WebApplication.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test","prod"})
class OAuth2LoginSuccessHandlerTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(new DefaultOidcUser(new ArrayList<>(), OidcIdToken
                    .withTokenValue("123123123")
                    .claim("name","someName")
                    .claim("at_hash","hash")
                    .subject("test")
                    .build()),"");
    @Autowired
    private OAuth2LoginSuccessHandler successHandler;

    @MockBean
    private UserServiceImpl service;
    @MockBean
    private JwtTokenProvider provider;

    @SneakyThrows
    @Test
    void onAuthenticationSuccess() {
        String token = "someToken";
        UserDto dto = new UserDto();
        dto.setUsername("someName");
        dto.setPassword("hash");

        when(service.oauth(dto)).thenReturn(dto);
        when(provider.createToken(dto)).thenReturn(token);
        doNothing().when(response).sendRedirect(any());

        successHandler.onAuthenticationSuccess(request,response,auth);

        verify(provider).createToken(dto);
    }
}