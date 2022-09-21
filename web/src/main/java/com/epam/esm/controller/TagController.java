package com.epam.esm.controller;


import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.TagHateoas;
import com.epam.esm.service.page.PageTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = "/api/v1/tags")
@Validated
@Profile("prod")
public class TagController {

    private final PageTagService service;

    private final TagHateoas hateoas;

    @Autowired
    public TagController(PageTagService service, TagHateoas hateoas) {
        this.service = service;
        this.hateoas = hateoas;
    }

    /**
     * @return all tags from database
     */
    @GetMapping
    public DtoPage<TagDto> getTags(@RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam(defaultValue = "id") String sort,
                                @RequestParam(defaultValue = "asc") String direction) throws ServiceException, RepositoryException {
        DtoPage<TagDto> dtoPage = service.findByActiveStatus(page,size,sort,direction);
        hateoas.setHateoas(dtoPage);
        return dtoPage;
    }

    @GetMapping("/top")
    public DtoPage<TagDto> getTopTags(@RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "3") Integer size) throws ServiceException, RepositoryException {
        DtoPage<TagDto> dtoPage = service.findTop(page,size);
        hateoas.setHateoas(dtoPage);
        return dtoPage;
    }

    /**
     * @param entity - from body
     * creating tag to database
     * @return create status
     */
    @PostMapping
    public DtoPage<TagDto> addTag(@Valid @RequestBody TagDto entity) throws RepositoryException, ServiceException {
        DtoPage<TagDto> page = service.save(entity);
        hateoas.setHateoas(page);
        return page;
    }

    /**
     * @param id from path
     * deleteWithResponse tag by id
     * @return deleteWithResponse status
     */
    @DeleteMapping("/{id}")
    public DtoPage<TagDto> deleteTag(@PathVariable @Min(1) long id) throws RepositoryException, ServiceException {
        DtoPage<TagDto> page = service.delete(id);
        hateoas.setHateoas(page);
        return page;
    }

    @PatchMapping("/{id}")
    public DtoPage<TagDto> updateTag(@Valid @RequestBody TagDto entity, @PathVariable @Min(1) long id) throws RepositoryException, ServiceException {
        DtoPage<TagDto> page = service.update(entity,id);
        hateoas.setHateoas(page);
        return page;
    }

    /**
     * @param id from path
     * @return from database by id
     */
    @GetMapping("/{id}")
    public DtoPage<TagDto> getByIdTag(@PathVariable @Min(1) long id) throws ServiceException, RepositoryException {
        DtoPage<TagDto> page = service.findById(id);
        hateoas.setHateoas(page);
        return page;
    }

    @GetMapping(
            value = "/{id}/img",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageByID(@PathVariable("id") @Min(1) long id) throws RepositoryException {
        return service.getImageByID(id);
    }

    @GetMapping("/search")
    public DtoPage<TagDto> search(TagDto dto) throws ServiceException, RepositoryException {
        DtoPage<TagDto> page = service.findByParam(dto);
        hateoas.setHateoas(page);
        return page;
    }

}
