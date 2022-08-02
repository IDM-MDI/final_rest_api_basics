package com.epam.esm.controller;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.UserHateoas;
import com.epam.esm.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final UserHateoas hateoas;

    @Autowired
    public UserController(UserService service, UserHateoas hateoas) {
        this.service = service;
        this.hateoas = hateoas;
    }

    /**
     * @return all tags from database
     */
    @GetMapping
    public DtoPage<UserDto> getUsers(@RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(defaultValue = "id") String sort) throws ServiceException, RepositoryException {
        DtoPage<UserDto> dtoPage = service.findAllPageWithDtoPage(page,size,sort);
        hateoas.setUserHateoas(dtoPage);
        return dtoPage;
    }

    @PostMapping
    public DtoPage<UserDto> registration(@RequestBody UserDto user) throws RepositoryException, ServiceException {
        DtoPage<UserDto> page = service.saveWithDtoPage(user);
        hateoas.setUserHateoas(page);
        return page;
    }

    /**
     * @param id from path
     * @return from database by id
     */
    @GetMapping("/{id}")
    public DtoPage<UserDto> getByIdUser(@PathVariable @Min(1) long id) throws RepositoryException, ServiceException {
        DtoPage<UserDto> page = service.findByIdWithDtoPage(id);
        hateoas.setUserHateoas(page);
        return page;
    }
    @GetMapping("/top")
    public DtoPage<UserDto> getTopUsers() throws ServiceException, RepositoryException {
        DtoPage<UserDto> page = service.findTopWithDtoPage();
        hateoas.setUserHateoas(page);
        return page;
    }

    @GetMapping("/search")
    public DtoPage<UserDto> search(UserDto dto) throws ServiceException, RepositoryException {
        DtoPage<UserDto> page = service.findByParamWithDtoPage(dto);
        hateoas.setUserHateoas(page);
        return page;
    }
}
