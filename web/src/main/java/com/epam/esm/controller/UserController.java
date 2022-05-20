package com.epam.esm.controller;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.PageHateoas;
import com.epam.esm.hateoas.impl.UserHateoas;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final UserHateoas hateoas;
    private final PageHateoas<UserDto> pageHateoas;

    @Autowired
    public UserController(UserService service, UserHateoas hateoas, PageHateoas<UserDto> pageHateoas) {
        this.service = service;
        this.hateoas = hateoas;
        this.pageHateoas = pageHateoas;
    }

    /**
     * @return all tags from database
     */
    @GetMapping
    public DtoPage<UserDto> getUsers(@RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(defaultValue = "id") String sort) {
        DtoPage<UserDto> dtoPage = service.findAll(page,size,sort);
        dtoPage.getContent().forEach(hateoas::addLinks);
        pageHateoas.addUsersPage(dtoPage);
        return dtoPage;
    }

    /**
     * @param entity - from body
     * creating tag to database
     * @return create status
     */
    @PostMapping
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDto entity) {
        service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

    /**
     * @param id from path
     * delete tag by id
     * @return delete status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable @Min(1) long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("deleted");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") @Min(1) long id,
                                             @Valid @RequestBody UserDto dto) throws ServiceException {
        service.update(dto,id);
        return ResponseEntity.status(HttpStatus.CREATED).body("updated");
    }

    /**
     * @param id from path
     * @return from database by id
     */
    @GetMapping("/{id}")
    public DtoPage<UserDto> getByIdUser(@PathVariable @Min(1) long id) {
        UserDto dto = service.findById(id);
        DtoPage<UserDto> page = new DtoPage<>();
        hateoas.addLinks(dto);
        page.setContent(List.of(dto));
        pageHateoas.addUserByIdPage(page);
        return page;
    }
}