package com.epam.esm.controller;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.hateoas.impl.OrderHateoas;
import com.epam.esm.security.JwtUserDetailsService;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.security.jwt.JwtUser;
import com.epam.esm.security.jwt.JwtUserFactory;
import com.epam.esm.service.LoginService;
import com.epam.esm.service.ResponseService;
import com.epam.esm.service.page.PageOrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = WebApplication.class)
@AutoConfigureMockMvc
class OrderControllerTest {

    private final ResponseService responseService = new ResponseService();
    private final DtoPageBuilder<OrderDto> builder = new DtoPageBuilder<>();
    private final MediaType halJson = MediaType.valueOf("application/hal+json");
    private final MediaType halJsonUTF = MediaType.valueOf("application/hal+json;charset=UTF-8");
    private final GiftCertificateDto certificate = new GiftCertificateDto(
            1L,
            "test",
            "testDescription",
            new BigDecimal("5"),
            5,
            null,
            null,
            List.of(
                    new TagDto(1L,"tag1",ACTIVE.name()),
                    new TagDto(2L,"tag2",ACTIVE.name())
            ),
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

    private final OrderDto dto = new OrderDto(
            1L,
            certificate.getPrice(),
            null,
            certificate,
            certificate.getId(),
            user.getId(),
            certificate.getStatus()
    );

    private final DtoPage<OrderDto> page = builder.setResponse(responseService.okResponse("ok"))
            .setContent(List.of(dto))
            .build();

    @Autowired
    private OrderController controller;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenProvider provider;

    @MockBean
    private PageOrderService service;
    @MockBean
    private OrderHateoas hateoas;
    @MockBean
    private LoginService loginService;
    @MockBean
    private JwtUserDetailsService userDetailsService;

    @SneakyThrows
    @Test
    void addOrder() {
        String token = provider.createToken(user);
        JwtUser jwtUser = JwtUserFactory.create(user);

        when(userDetailsService.loadUserByUsername(provider.getUsername(token)))
                .thenReturn(jwtUser);
        when(loginService.getUsername(any())).thenReturn(user.getUsername());
        when(service.save(user.getUsername(),certificate.getId())).thenReturn(page);
        doNothing().when(hateoas).setOrderHateoas(page);

        mockMvc.perform(post("/api/v1/order/1").with(csrf())
                        .header("Authorization","Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(halJson))
                .andExpect(jsonPath("$.content[0].id",is(Integer.valueOf(Long.toString(dto.getId())))))
                .andExpect(jsonPath("$.content[0].price",is(dto.getPrice().intValue())))
                .andExpect(jsonPath("$.content[0].giftId",is(Integer.valueOf(Long.toString(dto.getGiftId())))))
                .andExpect(jsonPath("$.content[0].userId",is(Integer.valueOf(Long.toString(dto.getUserId())))))
                .andExpect(jsonPath("$.response.code",is(200)));
    }
}