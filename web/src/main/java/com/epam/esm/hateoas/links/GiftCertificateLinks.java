package com.epam.esm.hateoas.links;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;

import static com.epam.esm.controller.ControllerClass.GIFT_CERTIFICATE_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class GiftCertificateLinks {
    private GiftCertificateLinks() {}
    public static void updateLink(GiftCertificateDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .updateGiftCertificate(dto.getId(), dto))
                .withRel("updateWithResponse"));
    }
    public static void addNewLink(GiftCertificateDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .addGiftCertificate(dto))
                .withRel("add"));
    }
    public static void deleteLink(GiftCertificateDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .deleteGiftCertificate(dto.getId()))
                .withRel("deleteWithResponse"));
    }
    public static void getByLink(GiftCertificateDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .getGiftCertificate(dto.getId()))
                .withSelfRel());
    }

    public static void getAllLink(DtoPage<GiftCertificateDto> dtoPage, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException {
        dtoPage.add(linkTo(methodOn(GIFT_CERTIFICATE_CONTROLLER).
                getAllGiftCertificate(number, size, sort,direction)).
                withRel(rel));
    }
    public static void getCertificateByTag(DtoPage<GiftCertificateDto> dtoPage,int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException {
        dtoPage.add(linkTo(methodOn(GIFT_CERTIFICATE_CONTROLLER).
                getGiftCertificatesByTag(Integer.parseInt(dtoPage.getParam()),number, size, sort, direction)).
                withRel(rel));
    }
}
