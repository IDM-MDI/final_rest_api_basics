package com.epam.esm.controller;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.hateoas.impl.UserHateoas;
import com.epam.esm.service.LoginService;
import com.epam.esm.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"test","prod"})
class IndexControllerTest {
    @MockBean
    private LoginService loginService;
    @MockBean
    private UserHateoas hateoas;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndexController controller;

    @Autowired
    private ResponseService responseService;
    private final MediaType textPlainType = MediaType.valueOf("text/plain;charset=UTF-8");
    private final MediaType halJson = MediaType.valueOf("application/hal+json");
    private final MediaType halJsonUTF = MediaType.valueOf("application/hal+json;charset=UTF-8");
    private UserDto user = new UserDto(1L,"usernameTest","passwordTest",null,null,null,null);
    private DtoPage<UserDto> page;

    @BeforeEach
    public void init() {
        DtoPageBuilder<UserDto> builder = new DtoPageBuilder<>();
        this.page = builder.setResponse(responseService.okResponse("ok"))
                .setContent(List.of(user))
                .build();
    }

    @SneakyThrows
    @Test
    void successLogin() {
        final String token = "someToken";
        this.mockMvc.perform(get("/api/v1/login/oauth/success?token=" + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content",contains("Your Bearer token is: someToken")));
    }

    @SneakyThrows
    @Test
    void login() {
        AuthenticationDto authentication = new AuthenticationDto("usernameTest","passwordTest");

        when(loginService.authenticate(authentication)).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        this.mockMvc.perform(post("/api/v1/login").with(csrf())
                        .contentType(halJsonUTF)
                        .content(new ObjectMapper().writeValueAsString(authentication)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(1)))
                .andExpect(jsonPath("$.content[0].username",is(user.getUsername()))
                );
    }

    @Test
    void jwtUserGuard() { //TODO: FINISH TEST
    }
}