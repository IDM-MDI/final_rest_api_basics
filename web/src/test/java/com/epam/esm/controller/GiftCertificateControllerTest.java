package com.epam.esm.controller;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.*;
import com.epam.esm.hateoas.impl.GiftCertificateHateoas;
import com.epam.esm.security.JwtUserDetailsService;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.JwtUserFactory;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.page.PageGiftCertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
class GiftCertificateControllerTest {

    @Autowired
    private GiftCertificateController controller;
    @MockBean
    private PageGiftCertificateService service;
    @MockBean
    private GiftCertificateHateoas hateoas;

    @Autowired
    private JwtTokenProvider provider;
    @MockBean
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;


    private final ResponseService responseService = new ResponseService();
    private final DtoPageBuilder<GiftCertificateDto> builder = new DtoPageBuilder<>();
    private final MediaType halJson = MediaType.valueOf("application/hal+json");
    private final MediaType halJsonUTF = MediaType.valueOf("application/hal+json;charset=UTF-8");
    private final GiftCertificateDto certificate = new GiftCertificateDto(
            1L,
            "test",
            "testDescription",
            new BigDecimal("5"),
            5,
            "shop 5",
            null,
            null,
            null,
            null,
            null,
            List.of(
                    new TagDto(1L,"tag1",null,false,ACTIVE.name()),
                    new TagDto(2L,"tag2",null,false,ACTIVE.name())
            ),false,false,false,
            ACTIVE.name()
    );

    private final UserDto user = new UserDto(
            1L,
            "usernameTest",
            "passwordTest",
            null,
            ACTIVE.name(),
            List.of(new RoleDto(1L,"ADMIN"),new RoleDto(2L,"USER")),
            null);

    private final DtoPage<GiftCertificateDto> page = builder.setResponse(responseService.okResponse("ok"))
                                                            .setContent(List.of(certificate))
                                                            .build();

    @SneakyThrows
    @Test
    void getAllGiftCertificate() {
        int size = 10;
        int pageNumber = 0;
        String sort = "id";
        String direction = "asc";

        when(service.findByActiveStatus(pageNumber,size,sort,direction))
                .thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(get("/api/v1/gifts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(certificate.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(certificate.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void addGiftCertificate() {
        String token = provider.createToken(user);
        JwtUser jwtUser = JwtUserFactory.create(user);

        when(userDetailsService.loadUserByUsername(provider.getUsername(token)))
                .thenReturn(jwtUser);
        when(service.save(certificate)).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(post("/api/v1/gifts").with(csrf())
                                .contentType(halJsonUTF)
                                .content(new ObjectMapper().writeValueAsString(certificate))
                                .header("Authorization","Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(certificate.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(certificate.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void deleteGiftCertificate() {
        String token = provider.createToken(user);
        JwtUser jwtUser = JwtUserFactory.create(user);

        when(userDetailsService.loadUserByUsername(provider.getUsername(token)))
                .thenReturn(jwtUser);
        when(service.delete(certificate.getId())).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(delete("/api/v1/gifts/1").with(csrf())
                        .header("Authorization","Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(certificate.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(certificate.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void updateGiftCertificate() {
        String token = provider.createToken(user);
        JwtUser jwtUser = JwtUserFactory.create(user);

        when(userDetailsService.loadUserByUsername(provider.getUsername(token)))
                .thenReturn(jwtUser);
        when(service.update(certificate,certificate.getId())).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(patch("/api/v1/gifts/1").with(csrf())
                        .contentType(halJsonUTF)
                        .content(new ObjectMapper().writeValueAsString(certificate))
                        .header("Authorization","Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(certificate.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(certificate.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void getGiftCertificate() {
        when(service.findById(certificate.getId())).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(get("/api/v1/gifts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(certificate.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(certificate.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void search() {
        GiftCertificateDto dtoSearch = new GiftCertificateDto(
                certificate.getId(),
                certificate.getName(),
                null,null,null,null,null,null,
                null,null,null,null,false,false,
                false,null
        );

        String tags = "tag1,tag2";
        when(service.findByParam(dtoSearch,tags)).thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(get("/api/v1/gifts/search")
                        .contentType(MediaType.TEXT_PLAIN)
                        .param("id",certificate.getId().toString())
                        .param("name",certificate.getName())
                        .param("tagList",tags))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(certificate.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(certificate.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void getGiftCertificatesByTag() {
        long id = 1;
        int size = 10;
        int pageNumber = 0;
        String sort = "id";
        String direction = "asc";

        when(service.findActiveByTag(id,pageNumber,size,sort,direction))
                .thenReturn(page);
        doNothing().when(hateoas).setHateoas(page);

        mockMvc.perform(get("/api/v1/gifts/tag/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(certificate.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name",is(certificate.getName())))
                .andExpect(jsonPath("$.response.code",is(200)));
    }

    @SneakyThrows
    @Test
    void getImageByID() {
        long id = 1;
        String name = "main";
        byte[] image = new byte[]{1,1,1,1,1};

        when(service.getImageByID(id,name)).thenReturn(image);

        mockMvc.perform(get("/api/v1/gifts/" + id + "/img"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }
}