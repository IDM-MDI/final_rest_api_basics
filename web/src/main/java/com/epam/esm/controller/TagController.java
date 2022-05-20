package com.epam.esm.controller;


import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.PageHateoas;
import com.epam.esm.hateoas.impl.TagHateoas;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequestMapping(value = "/tags")
@Validated
@Profile("prod")
public class TagController {

    private final TagService service;
    private final TagHateoas hateoas;
    private final PageHateoas<TagDto> pageHateoas;

    @Autowired
    public TagController(TagService service, TagHateoas hateoas, PageHateoas<TagDto> pageHateoas) {
        this.service = service;
        this.hateoas = hateoas;
        this.pageHateoas = pageHateoas;
    }

    /**
     * @return all tags from database
     */
    @GetMapping
    public DtoPage<TagDto> getTags(@RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam(defaultValue = "id") String sort) {
        DtoPage<TagDto> dtoPage = service.findAll(page,size,sort);
        dtoPage.getContent().forEach(hateoas::addLinks);
        pageHateoas.addTagsPage(dtoPage);
        return dtoPage;
    }

    /**
     * @param entity - from body
     * creating tag to database
     * @return create status
     * @throws ServiceException
     */
    @PostMapping
    public ResponseEntity<String> addTag(@Valid @RequestBody TagDto entity) throws ServiceException {
        service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

    /**
     * @param id from path
     * delete tag by id
     * @return delete status
     * @throws ServiceException
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable @Min(1) long id) throws ServiceException {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("deleted");
    }

    /**
     * @param id from path
     * @return from database by id
     */
    @GetMapping("/{id}")
    public DtoPage<TagDto> getByIdTag(@PathVariable @Min(1) long id) {
        TagDto dto = service.findById(id);
        DtoPage<TagDto> page = new DtoPage<>();
        hateoas.addLinks(dto);
        page.setContent(List.of(dto));
        pageHateoas.addTagByIdPage(page);
        return page;
    }
}
