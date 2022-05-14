package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
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

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * @return all tags from database
     */
    @GetMapping
    public List<UserDto> getUsers() {
        return service.findAll();
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

    /**
     * @param id from path
     * @return from database by id
     */
    @GetMapping("/{id}")
    public UserDto getByIdUser(@PathVariable @Min(1) long id) {
        return service.findById(id);
    }
}
