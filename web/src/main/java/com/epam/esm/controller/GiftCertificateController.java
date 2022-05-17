package com.epam.esm.controller;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.GiftCertificateHateoas;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    public GiftCertificateController(GiftCertificateService service, GiftCertificateHateoas hateoas) {
        this.service = service;
        this.hateoas = hateoas;
    }

    /**
     * @return all dto
     * @throws ServiceException
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getAllGiftCertificate(Pageable pageable) throws ServiceException {
        List<GiftCertificateDto> list = service.findAll(pageable);
        list.forEach(hateoas::addLinks);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    /**
     * @param entity from body parameter
     * @return create status
     * @throws ServiceException
     */
    @PostMapping
    public ResponseEntity<String> addGiftCertificate(@Valid @RequestBody GiftCertificateDto entity) throws ServiceException {
        service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

    /**
     * @param id from path
     * @return delete status
     * @throws ServiceException
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGiftCertificate(@PathVariable @Min(1) long id) throws ServiceException {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("deleted");
    }

    /**
     * @param id from path
     * @param entity from body
     * @return update status
     * @throws ServiceException
     */
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateGiftCertificate(@PathVariable("id") @Min(1) long id,
                                                        @Valid @RequestBody GiftCertificateDto entity) throws ServiceException {
        service.update(entity,id);
        return ResponseEntity.status(HttpStatus.CREATED).body("updated");
    }

    /**
     * @param id from path
     * @return find by id
     * @throws ServiceException
     */
    @GetMapping("/{id}")
    public GiftCertificateDto getGiftCertificate(@PathVariable("id") @Min(1) long id) throws ServiceException {
        GiftCertificateDto dto = service.findById(id);
        hateoas.addLinks(dto);
        return dto;
    }

    /**
     * @param param - parameter of requst param
     *              find by param
     * @return find dto
     */
    @GetMapping("/filter")
    public List<GiftCertificateDto> getByFilter(@RequestParam Map<String,String> param) {
        return service.findByParam(param);
    }
}