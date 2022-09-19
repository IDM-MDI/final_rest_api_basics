package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.links.GiftCertificateLinks.*;

@Component
public class GiftCertificateHateoas extends HateoasDTO<GiftCertificateDto> {
    private final TagHateoas tagHateoas;

    @Autowired
    public GiftCertificateHateoas(TagHateoas tagHateoas) {
        this.tagHateoas = tagHateoas;
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

    @Override
    protected void addPageLink(DtoPage<GiftCertificateDto> dtoPage, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException {
        if (dtoPage.getType() == ControllerType.CERTIFICATE_ALL) {
            getAllLink(dtoPage, number, size, sort, direction, rel);
        }
        else if (dtoPage.getType() == ControllerType.CERTIFICATE_BY_TAG) {
            getCertificateByTag(dtoPage, number, size, sort, direction, rel);
        }
    }
}
