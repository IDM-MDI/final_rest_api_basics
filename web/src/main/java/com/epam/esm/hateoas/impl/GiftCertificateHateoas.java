package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.GiftCertificateDto;
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

    @Autowired
    public GiftCertificateHateoas(TagHateoas tagHateoas) {
        this.tagHateoas = tagHateoas;
    }

    @Override
    public void addLinks(GiftCertificateDto dto) {
        try {
            getByLink(dto);
            addNewLink(dto);
            updateLink(dto);
            deleteLink(dto);
            dto.getTags().forEach(tagHateoas::addLinks);
        } catch (ServiceException | RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    private void updateLink(GiftCertificateDto dto) throws RepositoryException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .updateGiftCertificate(dto.getId(), dto))
                        .withRel("update"));
    }
    private void addNewLink(GiftCertificateDto dto) throws ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .addGiftCertificate(dto))
                        .withRel("add"));
    }
    private void deleteLink(GiftCertificateDto dto) throws ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .deleteGiftCertificate(dto.getId()))
                        .withRel("delete"));
    }
    private void getByLink(GiftCertificateDto dto) throws ServiceException {
        dto.add(linkTo(
                methodOn(GIFT_CERTIFICATE_CONTROLLER)
                        .getGiftCertificate(dto.getId()))
                        .withSelfRel());
    }
}
