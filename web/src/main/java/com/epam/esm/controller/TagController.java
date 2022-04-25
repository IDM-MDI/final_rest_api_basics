package com.epam.esm.controller;


import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.impl.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/tags")
@Validated
@Profile("prod")
public class TagController {

    private final TagService service;

    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }

    /**
     * @return all tags from database
     * @throws ServiceException
     */
    @GetMapping
    public List<TagDto> getTags() throws ServiceException {
        return service.findAll();
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
     * @throws ServiceException
     */
    @GetMapping("/{id}")
    public TagDto getByIdTag(@PathVariable @Min(1) long id) throws ServiceException {
        return service.findById(id);
    }
}
