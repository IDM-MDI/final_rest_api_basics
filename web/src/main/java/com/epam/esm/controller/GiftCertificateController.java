package com.epam.esm.controller;


import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.WebException;
import com.epam.esm.hateoas.impl.GiftCertificateHateoas;
import com.epam.esm.hateoas.impl.PageHateoas;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

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
    private final PageHateoas<GiftCertificateDto> pageHateoas;


    @Autowired
    public GiftCertificateController(GiftCertificateService service, GiftCertificateHateoas hateoas, PageHateoas<GiftCertificateDto> pageHateoas) {
        this.service = service;
        this.hateoas = hateoas;
        this.pageHateoas = pageHateoas;
    }

    /**
     * @return all dto
     */
    @GetMapping
    public DtoPage<GiftCertificateDto> getAllGiftCertificate(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size,
                                                          @RequestParam(defaultValue = "id") String sort) throws ServiceException, RepositoryException {
        DtoPage<GiftCertificateDto> dtoPage = service.findAll(page,size,sort);
        for (GiftCertificateDto dto : dtoPage.getContent()) {
            hateoas.addLinks(dto);
        }
        pageHateoas.addGiftsPage(dtoPage);
        return dtoPage;
    }

    /**
     * @param entity from body parameter
     * @return create status
     */
    @PostMapping
    public ResponseEntity<String> addGiftCertificate(@Valid @RequestBody GiftCertificateDto entity) {
        service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

    /**
     * @param id from path
     * @return delete status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGiftCertificate(@PathVariable @Min(1) long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("deleted");
    }

    /**
     * @param id from path
     * @param entity from body
     * @return update status
     */
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateGiftCertificate(@PathVariable("id") @Min(1) long id,
                                                        @Valid @RequestBody GiftCertificateDto entity) throws RepositoryException {
        service.update(entity,id);
        return ResponseEntity.status(HttpStatus.CREATED).body("updated");
    }

    /**
     * @param id from path
     * @return find by id
     */
    @GetMapping("/{id}")
    public DtoPage<GiftCertificateDto> getGiftCertificate(@PathVariable("id") @Min(1) long id) throws RepositoryException, ServiceException {
        DtoPage<GiftCertificateDto> page = service.findById(id);
        for (GiftCertificateDto dto : page.getContent()) {
            hateoas.addLinks(dto);
        }
        pageHateoas.addGiftGetBackPage(page);
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
        DtoPage<GiftCertificateDto> page = service.findAllByParam(dto,tagList);
        for (GiftCertificateDto giftCertificateDto : page.getContent()) {
            hateoas.addLinks(giftCertificateDto);
        }
        pageHateoas.addGiftGetBackPage(page);
        return page;
    }
}