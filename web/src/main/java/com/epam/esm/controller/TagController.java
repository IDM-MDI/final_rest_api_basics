package com.epam.esm.controller;


import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.PageHateoas;
import com.epam.esm.hateoas.impl.TagHateoas;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;


@RestController
@RequestMapping(value = "/tags")
@Validated
@Profile("prod")
public class TagController {

    private final TagService service;

    private final TagHateoas hateoas;

    @Autowired
    public TagController(TagService service, TagHateoas hateoas) {
        this.service = service;
        this.hateoas = hateoas;
    }

    /**
     * @return all tags from database
     */
    @GetMapping
    public DtoPage<TagDto> getTags(@RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam(defaultValue = "id") String sort) throws ServiceException, RepositoryException {
        DtoPage<TagDto> dtoPage = service.findAll(page,size,sort);
        hateoas.setTagHateoas(dtoPage);
        return dtoPage;
    }

    /**
     * @param entity - from body
     * creating tag to database
     * @return create status
     */
    @PostMapping
    public ResponseEntity<String> addTag(@Valid @RequestBody TagDto entity) {
        service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

    /**
     * @param id from path
     * delete tag by id
     * @return delete status
     */
    @DeleteMapping("/{id}")
    public ResponseDto<TagDto> deleteTag(@PathVariable @Min(1) long id) {
        return service.delete(id);
    }

    /**
     * @param id from path
     * @return from database by id
     */
    @GetMapping("/{id}")
    public DtoPage<TagDto> getByIdTag(@PathVariable @Min(1) long id) throws ServiceException, RepositoryException {
        DtoPage<TagDto> page = service.findById(id);
        hateoas.setTagHateoas(page);
        return page;
    }

    @GetMapping("/search")
    public DtoPage<TagDto> search(TagDto dto) throws ServiceException, RepositoryException {
        DtoPage<TagDto> page = service.findAllByParam(dto);
        hateoas.setTagHateoas(page);
        return page;
    }

}
