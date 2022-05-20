package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.DtoPage;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import static com.epam.esm.controller.ControllerClass.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PageHateoas<T> {
    private static final String PREVIOUS_PAGE = "prev";
    private static final String GET_BACK = "back";
    private static final String NEXT_PAGE = "next";
    private static final String SIZE_5 = "size-5";
    private static final String SIZE_10 = "size-10";
    private static final String SIZE_15 = "size-15";
    private static final String SORT_BY_ID = "sort-by-id";
    private static final String SORT_BY_NAME = "sort-by-name";
    private static final String SORT_BY_PRICE = "sort-by-price";
    private final int sizeOfFive = 5;
    private final int sizeOfTen = 10;
    private final int sizeOfFifth = 15;


    private int thisSize, thisPage;
    private int prevPage, nextPage;
    private String thisSort;
    private final String id = "id";
    private final String name = "name";
    private final String price = "price";


    public void addGiftsPage(DtoPage<T> dtoPage) {
        init(dtoPage);
        if(prevPage >= 0)
            addLinkGift(dtoPage,prevPage,thisSize,thisSort,PREVIOUS_PAGE);
        if(dtoPage.getContent().size() == thisSize)
            addLinkGift(dtoPage,nextPage,thisSize,thisSort,NEXT_PAGE);

        addLinkGift(dtoPage,thisPage,sizeOfFive,thisSort,SIZE_5);
        addLinkGift(dtoPage,thisPage,sizeOfTen,thisSort,SIZE_10);
        addLinkGift(dtoPage,thisPage,sizeOfFifth,thisSort,SIZE_15);

        addLinkGift(dtoPage,thisPage,thisSize,id,SORT_BY_ID);
        addLinkGift(dtoPage,thisPage,thisSize,name,SORT_BY_NAME);
        addLinkGift(dtoPage,thisPage,thisSize,price,SORT_BY_PRICE);
    }
    public void addTagsPage(DtoPage<T> dtoPage) {
        init(dtoPage);
        if(prevPage >= 0)
            addLinkTag(dtoPage,prevPage,thisSize,thisSort,PREVIOUS_PAGE);
        if(dtoPage.getContent().size() == thisSize)
            addLinkTag(dtoPage,nextPage,thisSize,thisSort,NEXT_PAGE);

        addLinkTag(dtoPage,thisPage,sizeOfFive,thisSort,SIZE_5);
        addLinkTag(dtoPage,thisPage,sizeOfTen,thisSort,SIZE_10);
        addLinkTag(dtoPage,thisPage,sizeOfFifth,thisSort,SIZE_15);

        addLinkTag(dtoPage,thisPage,thisSize,id,SORT_BY_ID);
        addLinkTag(dtoPage,thisPage,thisSize,name,SORT_BY_NAME);
    }

    public void addUsersPage(DtoPage<T> dtoPage) {
        init(dtoPage);
        if(prevPage >= 0)
            addLinkUser(dtoPage,prevPage,thisSize,thisSort,PREVIOUS_PAGE);
        if(dtoPage.getContent().size() == thisSize)
            addLinkUser(dtoPage,nextPage,thisSize,thisSort,NEXT_PAGE);

        addLinkUser(dtoPage,thisPage,sizeOfFive,thisSort,SIZE_5);
        addLinkUser(dtoPage,thisPage,sizeOfTen,thisSort,SIZE_10);
        addLinkUser(dtoPage,thisPage,sizeOfFifth,thisSort,SIZE_15);

        addLinkUser(dtoPage,thisPage,thisSize,id,SORT_BY_ID);
        addLinkUser(dtoPage,thisPage,thisSize,name,SORT_BY_NAME);
    }

    public void addGiftByIdPage(DtoPage<T> page) {
        addLinkGift(page,0,10,id,GET_BACK);
    }
    public void addTagByIdPage(DtoPage<T> page) {
        addLinkTag(page,0,10,id,GET_BACK);
    }
    public void addUserByIdPage(DtoPage<T> page) {
        addLinkUser(page,0,10,id,GET_BACK);
    }
    private void init(DtoPage<T> dtoPage) {
        thisSize = dtoPage.getSize();
        thisPage = dtoPage.getNumberOfPage();
        prevPage = thisPage - 1;
        nextPage = thisPage + 1;
        thisSort = dtoPage.getSortBy();
    }
    @SneakyThrows
    private void addLinkGift(DtoPage<T> dtoPage, int number, int size, String sort, String rel) {
        dtoPage.add(linkTo(methodOn(GIFT_CERTIFICATE_CONTROLLER).
                getAllGiftCertificate(number, size, sort)).
                withRel(rel));
    }
    @SneakyThrows
    private void addLinkTag(DtoPage<T> dtoPage, int number, int size, String sort, String rel) {
        dtoPage.add(linkTo(methodOn(TAG_CONTROLLER).
                getTags(number, size, sort)).
                withRel(rel));
    }

    @SneakyThrows
    private void addLinkUser(DtoPage<T> dtoPage, int number, int size, String sort, String rel) {
        dtoPage.add(linkTo(methodOn(USER_CONTROLLER).
                getUsers(number, size, sort)).
                withRel(rel));
    }
}
