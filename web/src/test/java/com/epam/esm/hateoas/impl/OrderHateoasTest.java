package com.epam.esm.hateoas.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.util.impl.GiftCertificateModelMapper;
import com.epam.esm.util.impl.RoleModelMapper;
import com.epam.esm.util.impl.TagModelMapper;
import com.epam.esm.util.impl.UserModelMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;

@SpringBootTest(classes = WebApplication.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test","prod"})
class OrderHateoasTest {
    private static final int PAGE = 1;
    private static final int SIZE = 0;
    private static final String SORT = "ID";
    private static final String DIRECTION = "asc";

    private final OrderDto dto;
    private final Order entity;
    private final User userEntity;
    private final UserDto userDto;
    private final UserModelMapper userMapper;
    private final GiftCertificateModelMapper giftMapper;
    private final GiftCertificate giftEntity;
    private final GiftCertificateDto giftDto;

    @Autowired
    private OrderHateoas hateoas;

    OrderHateoasTest() {
        List<Role> role = List.of(new Role(1L, "user"));
        this.giftMapper = new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder());
        this.userMapper = new UserModelMapper(new UserBuilder(),giftMapper,new RoleModelMapper());

        this.userEntity = new User(1L,"username","password",null,role,ACTIVE.name());
        this.userDto = userMapper.toDto(userEntity);

        this.giftEntity = new GiftCertificate(
                1L,
                "test",
                "test",
                new BigDecimal("0"),
                1,
                null,
                null,
                new ArrayList<>(),
                null,
                null,
                null,
                null,
                ACTIVE.name()
        );
        giftDto = giftMapper.toDto(giftEntity);
        dto = new OrderDto(
                1L,
                giftDto.getPrice(),
                null,
                giftDto,
                giftDto.getId(),
                userDto.getId(),
                ACTIVE.name()
        );
        entity = new Order(
                1L,
                giftDto.getPrice(),
                null,
                giftEntity,
                userEntity,
                ACTIVE.name()
        );
    }

    @SneakyThrows
    @Test
    void setHateoas() {
        DtoPage<OrderDto> expected = new DtoPage<>(
                List.of(dto),null,SIZE,PAGE,SORT,DIRECTION,true,null, ControllerType.ORDER_USER
        );
        hateoas.setHateoas(expected);
        Assertions.assertTrue(isLinksNotEmpty(dto));
    }
    public boolean isLinksNotEmpty(OrderDto order) {
        return !order.getGift().getLinks().isEmpty();
    }
}