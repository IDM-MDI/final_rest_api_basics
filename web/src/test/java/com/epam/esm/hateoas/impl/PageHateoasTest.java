package com.epam.esm.hateoas.impl;

import com.epam.esm.builder.impl.DtoPageBuilder;
import com.epam.esm.config.WebApplication;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.ResponseService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.epam.esm.controller.ControllerClass.GIFT_CERTIFICATE_CONTROLLER;
import static com.epam.esm.controller.ControllerClass.TAG_CONTROLLER;
import static com.epam.esm.controller.ControllerClass.USER_CONTROLLER;
import static com.epam.esm.entity.StatusName.ACTIVE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SpringBootTest(classes = WebApplication.class)
class PageHateoasTest {

    @Autowired
    private PageHateoas<TagDto> tagHateoas;
    @Autowired
    private PageHateoas<UserDto> userHateoas;
    @Autowired
    private PageHateoas<GiftCertificateDto> giftHateoas;
    @Autowired
    private ResponseService responseService;

    private static final DtoPageBuilder<GiftCertificateDto> giftDtoPageBuilder = new DtoPageBuilder<>();
    private static final DtoPageBuilder<TagDto> tagDtoPageBuilder = new DtoPageBuilder<>();
    private static final DtoPageBuilder<UserDto> userDtoPageBuilder = new DtoPageBuilder<>();


    @SneakyThrows
    @Test
    void addGiftsPage() {
        List<GiftCertificateDto> content = List.of(new GiftCertificateDto(1L, "name", "desc", null, null, null, null, null,ACTIVE.name()));
        DtoPage<GiftCertificateDto> actual = giftDtoPageBuilder
                .setContent(content)
                .setResponse(responseService.okResponse("ok"))
                .setSize(1)
                .setNumberOfPage(1)
                .setSortBy("sort")
                .build();
        giftHateoas.addGiftsPage(actual);
        Assertions.assertNotNull(actual.getLinks());
    }

    @SneakyThrows
    @Test
    void addTagsPage() {
        List<TagDto> content = List.of(new TagDto(1L, "name",ACTIVE.name()));
        DtoPage<TagDto> actual = tagDtoPageBuilder
                .setContent(content)
                .setResponse(responseService.okResponse("ok"))
                .setSize(1)
                .setNumberOfPage(1)
                .setSortBy("sort")
                .build();
        tagHateoas.addTagsPage(actual);
        Assertions.assertNotNull(actual.getLinks());
    }

    @SneakyThrows
    @Test
    void addUsersPage() {
        List<UserDto> content = List.of(new UserDto(1L, "login","password",null,null,null,null));
        DtoPage<UserDto> actual = userDtoPageBuilder
                .setContent(content)
                .setResponse(responseService.okResponse("ok"))
                .setSize(1)
                .setNumberOfPage(1)
                .setSortBy("sort")
                .build();
        userHateoas.addUsersPage(actual);
        Assertions.assertNotNull(actual.getLinks());
    }

    @SneakyThrows
    @Test
    void addGiftGetBackPage() {
        List<GiftCertificateDto> content = List.of(new GiftCertificateDto(1L, "name", "desc", null, null, null, null, null,ACTIVE.name()));

        DtoPage<GiftCertificateDto> expected = giftDtoPageBuilder
                .setContent(content)
                .setResponse(responseService.okResponse("ok"))
                .setSize(1)
                .setNumberOfPage(1)
                .setSortBy("sort")
                .build();
        DtoPage<GiftCertificateDto> actual = giftDtoPageBuilder
                .setContent(content)
                .setResponse(responseService.okResponse("ok"))
                .setSize(1)
                .setNumberOfPage(1)
                .setSortBy("sort")
                .build();

        giftHateoas.addGiftGetBackPage(actual);

        expected.add(linkTo(methodOn(GIFT_CERTIFICATE_CONTROLLER).
                getAllGiftCertificate(0, 10, "id")).
                withRel("back"));

        Assertions.assertEquals(expected.getLinks(),actual.getLinks());
    }

    @SneakyThrows
    @Test
    void addTagGetBackPage() {
        List<TagDto> content = List.of(new TagDto(1L, "name",ACTIVE.name()));

        DtoPage<TagDto> expected = tagDtoPageBuilder
                .setContent(content)
                .setResponse(responseService.okResponse("ok"))
                .setSize(1)
                .setNumberOfPage(1)
                .setSortBy("sort")
                .build();
        DtoPage<TagDto> actual = tagDtoPageBuilder
                .setContent(content)
                .setResponse(responseService.okResponse("ok"))
                .setSize(1)
                .setNumberOfPage(1)
                .setSortBy("sort")
                .build();

        tagHateoas.addTagGetBackPage(actual);

        expected.add(linkTo(methodOn(TAG_CONTROLLER).
                getTags(0, 10, "id")).
                withRel("back"));

        Assertions.assertEquals(expected.getLinks(),actual.getLinks());
    }

    @SneakyThrows
    @Test
    void addUserGetBackPage() {
        List<UserDto> content = List.of(new UserDto(1L, "login","password",null,null,null,null));
        DtoPage<UserDto> expected = userDtoPageBuilder
                .setContent(content)
                .setResponse(responseService.okResponse("ok"))
                .setSize(1)
                .setNumberOfPage(1)
                .setSortBy("sort")
                .build();
        DtoPage<UserDto> actual = userDtoPageBuilder
                .setContent(content)
                .setResponse(responseService.okResponse("ok"))
                .setSize(1)
                .setNumberOfPage(1)
                .setSortBy("sort")
                .build();
        userHateoas.addUserGetBackPage(actual);
        expected.add(linkTo(methodOn(USER_CONTROLLER).
                getUsers(0, 10, "id")).
                withRel("back"));
        Assertions.assertEquals(expected.getLinks(),actual.getLinks());
    }
}