package com.epam.esm.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
@Profile("prod")
public class IndexController {

    @GetMapping
    public String empty() {
        return "";
    }
}
