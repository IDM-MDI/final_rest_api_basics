package com.epam.esm.controller;

import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.UserDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController controller;

    private final MediaType contentType = new MediaType(
            "application",
            "hal+json"
    );

    @SneakyThrows
    @Test
    void getUsers() {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content[0].id", anything()))
                .andExpect(jsonPath("$.content[0].username", anything()))
                .andExpect(jsonPath("$.content[0].password", anything())
                );
    }

    @Test
    void registration() {
    }

    @Test
    void getByIdUser() {
    }

    @Test
    void getTopUsers() {
    }

    @Test
    void search() {
    }
}