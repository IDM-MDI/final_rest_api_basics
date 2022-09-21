package com.epam.esm.controller;


import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.GiftCertificateHateoas;
import com.epam.esm.service.page.PageGiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Class created for catch /gits - url
 * Have CRUD and filter api
 */
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = "/api/v1/gifts")
@Validated
@Profile("prod")
public class GiftCertificateController {

    private final PageGiftCertificateService service;
    private final GiftCertificateHateoas hateoas;


    @Autowired
    public GiftCertificateController(PageGiftCertificateService service, GiftCertificateHateoas hateoas) {
        this.service = service;
        this.hateoas = hateoas;
    }

    /**
     * @return all dto
     */
    @GetMapping
    public DtoPage<GiftCertificateDto> getAllGiftCertificate(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size,
                                                          @RequestParam(defaultValue = "id") String sort,
                                                          @RequestParam(defaultValue = "asc") String direction) throws ServiceException, RepositoryException {
        DtoPage<GiftCertificateDto> dtoPage = service.findByActiveStatus(page,size,sort,direction);
        hateoas.setHateoas(dtoPage);
        return dtoPage;
    }

    @GetMapping("/tag/{id}")
    public DtoPage<GiftCertificateDto> getGiftCertificatesByTag(@PathVariable @Min(1) long id,
                                                                @RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer size,
                                                                @RequestParam(defaultValue = "id") String sort,
                                                                @RequestParam(defaultValue = "asc") String direction) throws ServiceException, RepositoryException {
        DtoPage<GiftCertificateDto> dtoPage = service.findActiveByTag(id,page,size,sort,direction);
        hateoas.setHateoas(dtoPage);
        return dtoPage;
    }

    /**
     * @param entity from body parameter
     * @return create status
     */
    @PostMapping
    public DtoPage<GiftCertificateDto> addGiftCertificate(@Valid @RequestBody GiftCertificateDto entity) throws RepositoryException, ServiceException {
        DtoPage<GiftCertificateDto> page = service.save(entity);
        hateoas.setHateoas(page);
        return page;
    }

    /**
     * @param id from path
     * @return deleteWithResponse status
     */
    @DeleteMapping("/{id}")
    public DtoPage<GiftCertificateDto> deleteGiftCertificate(@PathVariable @Min(1) long id) throws RepositoryException, ServiceException {
        DtoPage<GiftCertificateDto> page = service.delete(id);
        hateoas.setHateoas(page);
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
        DtoPage<GiftCertificateDto> page = service.update(entity, id);
        hateoas.setHateoas(page);
        return page;
    }

    /**
     * @param id from path
     * @return find by id
     */
    @GetMapping("/{id}")
    public DtoPage<GiftCertificateDto> getGiftCertificate(@PathVariable("id") @Min(1) long id) throws RepositoryException, ServiceException {
        DtoPage<GiftCertificateDto> page = service.findById(id);
        hateoas.setHateoas(page);
        return page;
    }

    @GetMapping(
            value = "/{id}/img",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageByID(@PathVariable("id") @Min(1) long id, @RequestParam(defaultValue = "main")String name) throws RepositoryException {
        return service.getImageByID(id,name);
    }

    /**
     * @param dto - parameter of requst param
     *              find by param
     * @return find dto
     */
    @GetMapping("/search")
    public DtoPage<GiftCertificateDto> search(GiftCertificateDto dto,
                                              @RequestParam(defaultValue = "") String tagList) throws ServiceException, RepositoryException {
        DtoPage<GiftCertificateDto> page = service.findByParam(dto,tagList);
        hateoas.setHateoas(page);
        return page;
    }
}