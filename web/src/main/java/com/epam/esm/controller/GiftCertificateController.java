package com.epam.esm.controller;


import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.GiftCertificateHateoas;
import com.epam.esm.service.impl.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Class created for catch /gits - url
 * Have CRUD and filter api
 */
@RestController
@RequestMapping(value = "/gifts")
@Validated
@Profile("prod")
public class GiftCertificateController {

    private final GiftCertificateService service;
    private final GiftCertificateHateoas hateoas;


    @Autowired
    public GiftCertificateController(GiftCertificateService service, GiftCertificateHateoas hateoas) {
        this.service = service;
        this.hateoas = hateoas;
    }

    /**
     * @return all dto
     */
    @GetMapping
    public DtoPage<GiftCertificateDto> getAllGiftCertificate(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size,
                                                          @RequestParam(defaultValue = "id") String sort) throws ServiceException, RepositoryException {
        DtoPage<GiftCertificateDto> dtoPage = service.findAllWithPage(page,size,sort);
        hateoas.setGiftHateoas(dtoPage);
        return dtoPage;
    }

    /**
     * @param entity from body parameter
     * @return create status
     */
    @PostMapping
    public DtoPage<GiftCertificateDto> addGiftCertificate(@Valid @RequestBody GiftCertificateDto entity) throws RepositoryException, ServiceException {
        DtoPage<GiftCertificateDto> page = service.saveWithResponse(entity);
        hateoas.setGiftHateoas(page);
        return page;
    }

    /**
     * @param id from path
     * @return deleteWithResponse status
     */
    @DeleteMapping("/{id}")
    public DtoPage<GiftCertificateDto> deleteGiftCertificate(@PathVariable @Min(1) long id) throws RepositoryException, ServiceException {
        DtoPage<GiftCertificateDto> page = service.deleteWithDtoPage(id);
        hateoas.setGiftHateoas(page);
        return page;
    }

    /**
     * @param id from path
     * @param entity from body
     * @return updateWithResponse status
     */
    @PatchMapping("/{id}")
    public DtoPage<GiftCertificateDto> updateGiftCertificate(@PathVariable("id") @Min(1) long id,
                                             @Valid @RequestBody GiftCertificateDto entity) throws RepositoryException, ServiceException {
        DtoPage<GiftCertificateDto> page = service.updateWithDtoPage(entity, id);
        hateoas.setGiftHateoas(page);
        return page;
    }

    /**
     * @param id from path
     * @return find by id
     */
    @GetMapping("/{id}")
    public DtoPage<GiftCertificateDto> getGiftCertificate(@PathVariable("id") @Min(1) long id) throws RepositoryException, ServiceException {
        DtoPage<GiftCertificateDto> page = service.findByIdWithPage(id);
        hateoas.setGiftHateoas(page);
        return page;
    }

    /**
     * @param dto - parameter of requst param
     *              find by param
     * @return find dto
     */
    @GetMapping("/search")
    public DtoPage<GiftCertificateDto> search(GiftCertificateDto dto,
                                              @RequestParam(defaultValue = "") String tagList) throws ServiceException, RepositoryException {
        DtoPage<GiftCertificateDto> page = service.findByParamWithDtoPage(dto,tagList);
        hateoas.setGiftHateoas(page);
        return page;
    }
}