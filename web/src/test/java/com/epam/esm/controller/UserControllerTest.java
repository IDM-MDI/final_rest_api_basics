package com.epam.esm.controller;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.hateoas.impl.UserHateoas;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.page.PageUserService;
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

import static com.epam.esm.entity.StatusName.ACTIVE;
import static org.hamcrest.Matchers.anything;
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
class UserControllerTest {

    private final MediaType halJson = MediaType.valueOf("application/hal+json");
    private final MediaType halJsonUTF = MediaType.valueOf("application/hal+json;charset=UTF-8");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PageUserService service;
    @MockBean
    private UserHateoas hateoas;


    @Autowired
    private UserController controller;

    private final MediaType contentType = new MediaType(
            "application",
            "hal+json"
    );

    @Autowired
    private ResponseService responseService;


    private DtoPageBuilder<UserDto> builder = new DtoPageBuilder<>();

    private UserDto dto = new UserDto(
            1L,
            "usernameTest",
            null,
            null,
            ACTIVE.name(),
            List.of(new RoleDto(1L,"ADMIN"),new RoleDto(2L,"USER")),
            null
    );
    private User entity = new User(
            1L,
            "usernameTest",
            "passwordTest",
            null,
            List.of(new Role(1L,"ADMIN"),new Role(2L,"USER")),
            ACTIVE.name());
    private DtoPage<UserDto> page;

    @BeforeEach
    public void init() {
        page =  builder
                .setContent(List.of(dto))
                .setResponse(responseService.okResponse("ok"))
                .build();
    }

    @SneakyThrows
    @Test
    void getUsers() {
        int pageNumber = 0;
        int size = 10;
        String sort = "id";
        String direction = "asc";

        when(service.findByActiveStatus(pageNumber,size,sort,direction)).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        this.mockMvc.perform(get("/api/v1/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content[0].id", anything()))
                .andExpect(jsonPath("$.content[0].username", anything())
                );
    }

    @SneakyThrows
    @Test
    void registration() {
        when(service.save(dto)).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        this.mockMvc.perform(post("/api/v1/users").with(csrf())
                        .contentType(halJson)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content[0].id", anything()))
                .andExpect(jsonPath("$.content[0].username", anything())
                );
    }

    @SneakyThrows
    @Test
    void getByIdUser() {
        when(service.findById(dto.getId())).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        this.mockMvc.perform(get("/api/v1/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content[0].id", anything()))
                .andExpect(jsonPath("$.content[0].username", anything())
                );
    }

    @SneakyThrows
    @Test
    void getTopUsers() {
        when(service.findTop()).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        this.mockMvc.perform(get("/api/v1/users/top"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content[0].id", anything()))
                .andExpect(jsonPath("$.content[0].username", anything())
                );
    }

    @SneakyThrows
    @Test
    void search() {
        UserDto user = new UserDto();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());

        when(service.findByParam(user)).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        this.mockMvc.perform(get("/api/v1/users/search")
                        .param("id",String.valueOf(dto.getId()))
                        .param("username",dto.getUsername()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content[0].id", anything()))
                .andExpect(jsonPath("$.content[0].username", anything())
                );
    }
}