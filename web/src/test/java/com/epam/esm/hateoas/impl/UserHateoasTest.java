package com.epam.esm.hateoas.impl;

import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
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
class UserHateoasTest {
    private static final int PAGE = 1;
    private static final int SIZE = 0;
    private static final String SORT = "ID";
    private static final String DIRECTION = "asc";

    private static final GiftCertificateDto gift = new GiftCertificateDto(
            1L,
            "name",
            "description",
            new BigDecimal("0"),
            0,
            "shop",
            null,
            null,
            null,
            null,
            null,
            new ArrayList<>(),
            false,
            false,
            false,
            "testStatus"
    );
    private static final UserDto dto = new UserDto(
            1L,
            "username",
            "password",
            List.of(new OrderDto(
                    1L,
                    gift.getPrice(),
                    null,
                    gift,
                    gift.getId(),
                    1L,
                    ACTIVE.name()
            )),
            null,
            null,
            null
    );
    @Autowired
    private UserHateoas hateoas;
    @SneakyThrows
    @Test
    void setHateoasUserAll() {
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),null,SIZE,PAGE,SORT,DIRECTION,true,null, ControllerType.USER_ALL);
        hateoas.setHateoas(expected);
        Assertions.assertTrue(isLinksNotEmpty(expected.getContent()));
    }

    @SneakyThrows
    @Test
    void setHateoasUserByTop() {
        DtoPage<UserDto> expected = new DtoPage<>(List.of(dto),null,SIZE,PAGE,SORT,DIRECTION,true,null, ControllerType.USER_BY_TOP);
        hateoas.setHateoas(expected);
        Assertions.assertTrue(isLinksNotEmpty(expected.getContent()));
    }

    public boolean isLinksNotEmpty(List<UserDto> list) {
        for (UserDto i : list) {
            if(i.getLinks().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}