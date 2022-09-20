package com.epam.esm.hateoas.impl;

import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
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

@SpringBootTest(classes = WebApplication.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test","prod"})
class GiftCertificateHateoasTest {
    private static final int PAGE = 0;
    private static final int SIZE = 0;
    private static final String SORT = "ID";
    private static final String DIRECTION = "asc";

    @Autowired
    private GiftCertificateHateoas hateoas;

    private final List<GiftCertificateDto> dtoList = List.of(
            new GiftCertificateDto(
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
            ),
            new GiftCertificateDto(
                    2L,
                    "name 2",
                    "description 2",
                    new BigDecimal("2"),
                    2,
                    "shop 2",
                    null,
                    null,
                    null,
                    null,
                    null,
                    new ArrayList<>(),
                    false,
                    false,
                    false,
                    "testStatus"),
                    new GiftCertificateDto(
                    3L,
                    "name 3",
                    "description 3",
                    new BigDecimal("3"),
                    3,
                    "shop 3",
                    null,
                    null,
                    null,
                    null,
                    null,
                    new ArrayList<>(),
                    false,
                    false,
                    false,
                    "testStatus"));

    @SneakyThrows
    @Test
    void setHateoasAll() {
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(
                dtoList,
                null,
                SIZE,
                PAGE,
                SORT,
                DIRECTION,
                false,
                null,
                ControllerType.CERTIFICATE_ALL
        );
        hateoas.setHateoas(expected);
        Assertions.assertTrue(isLinksNotEmpty(expected.getContent()));
    }

    @SneakyThrows
    @Test
    void setHateoasByTag() {
        DtoPage<GiftCertificateDto> expected = new DtoPage<>(
                dtoList,
                null,
                SIZE,
                PAGE,
                SORT,
                DIRECTION,
                false,
                null,
                ControllerType.CERTIFICATE_BY_TAG
        );
        hateoas.setHateoas(expected);
        Assertions.assertTrue(isLinksNotEmpty(expected.getContent()));
    }

    public boolean isLinksNotEmpty(List<GiftCertificateDto> list) {
        for (GiftCertificateDto i : list) {
            if(i.getLinks().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}