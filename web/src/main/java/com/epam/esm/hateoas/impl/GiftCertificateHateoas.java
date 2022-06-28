package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.esm.controller.ControllerClass.GIFT_CERTIFICATE_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateHateoas implements HateoasDTO<GiftCertificateDto> {

    private final TagHateoas tagHateoas;
    private final PageHateoas<GiftCertificateDto> pageHateoas;

    @Autowired
    public GiftCertificateHateoas(TagHateoas tagHateoas, PageHateoas<GiftCertificateDto> pageHateoas) {
        this.tagHateoas = tagHateoas;
        this.pageHateoas = pageHateoas;
    }

    @Override
    public void addLinks(GiftCertificateDto dto) throws ServiceException, RepositoryException {
        getByLink(dto);
        addNewLink(dto);
        updateLink(dto);
        deleteLink(dto);
        for (TagDto tagDto : dto.getTags()) {
            tagHateoas.addLinks(tagDto);
        }
    }
    private void updateLink(GiftCertificateDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .updateGiftCertificate(dto.getId(), dto))
                        .withRel("updateWithResponse"));
    }
    private void addNewLink(GiftCertificateDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .addGiftCertificate(dto))
                        .withRel("add"));
    }
    private void deleteLink(GiftCertificateDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .deleteGiftCertificate(dto.getId()))
                        .withRel("deleteWithResponse"));
    }
    private void getByLink(GiftCertificateDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .getGiftCertificate(dto.getId()))
                        .withSelfRel());
    }

    public void setGiftHateoas(DtoPage<GiftCertificateDto> dtoPage) throws ServiceException, RepositoryException {
        for (GiftCertificateDto dto : dtoPage.getContent()) {
            addLinks(dto);
        }
        if(dtoPage.getSize() == 0) {
            pageHateoas.addGiftGetBackPage(dtoPage);
        }
        else {
            pageHateoas.addGiftsPage(dtoPage);
        }
    }
}
