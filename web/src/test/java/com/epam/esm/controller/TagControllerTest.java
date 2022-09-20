package com.epam.esm.controller;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.hateoas.impl.TagHateoas;
import com.epam.esm.security.JwtUserDetailsService;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.JwtUserFactory;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.page.PageTagService;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"test","prod"})
class TagControllerTest {
    @Autowired
    private TagController controller;
    @MockBean
    private PageTagService service;
    @MockBean
    private TagHateoas hateoas;

    @Autowired
    private JwtTokenProvider provider;
    @MockBean
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResponseService responseService;

    private TagDto tag = new TagDto(1L,"test",null,false,ACTIVE.name());
    private UserDto user = new UserDto(
            1L,
            "usernameTest",
            "passwordTest",
            null,
            ACTIVE.name(),
            List.of(new RoleDto(1L,"ADMIN"),new RoleDto(2L,"USER")),
            null);
    private DtoPage<TagDto> page;

    private final MediaType halJson = MediaType.valueOf("application/hal+json");
    private final MediaType halJsonUTF = MediaType.valueOf("application/hal+json;charset=UTF-8");

    @BeforeEach
    public void init() {
        DtoPageBuilder<TagDto> builder = new DtoPageBuilder<>();
        this.page = builder
                .setResponse(responseService.okResponse("ok"))
                .setContent(List.of(tag))
                .build();
    }

    @SneakyThrows
    @Test
    void getTags() {
        int pageNumber = 0;
        int size = 10;
        String sort = "id";
        String direction = "asc";

        when(service.findByActiveStatus(pageNumber,size,sort,direction)).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(get("/api/v1/tags"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(tag.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(tag.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void addTag() {
        String token = provider.createToken(user);
        JwtUser jwtUser = JwtUserFactory.create(user);

        when(userDetailsService.loadUserByUsername(provider.getUsername(token)))
                .thenReturn(jwtUser);
        when(service.save(tag)).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(post("/api/v1/tags").with(csrf())
                        .header("Authorization","Bearer " + token)
                        .contentType(halJsonUTF)
                        .content(new ObjectMapper().writeValueAsString(tag)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(tag.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(tag.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void deleteTag() {
        String token = provider.createToken(user);
        JwtUser jwtUser = JwtUserFactory.create(user);

        when(userDetailsService.loadUserByUsername(provider.getUsername(token)))
                .thenReturn(jwtUser);
        when(service.delete(tag.getId())).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(delete("/api/v1/tags/1").with(csrf())
                        .header("Authorization","Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(tag.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(tag.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void getByIdTag() {
        when(service.findById(tag.getId())).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(get("/api/v1/tags/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(tag.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(tag.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void search() {
        when(service.findByParam(tag)).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(get("/api/v1/tags/search?id=" + tag.getId() +
                        "&name=" + tag.getName() +
                        "&status=" + tag.getStatus()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(tag.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(tag.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @Test
    void getTopTags() { //TODO: FINISH TEST
    }

    @Test
    void updateTag() { //TODO: FINISH TEST
    }

    @Test
    void getImageByID() { //TODO: FINISH TEST
    }
}