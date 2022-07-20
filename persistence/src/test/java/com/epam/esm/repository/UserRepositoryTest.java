package com.epam.esm.repository;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserBuilder builder;

    public void init() {
        statusRepository.saveAll(List.of(new Status(null,"active"),new Status(null,"deleted")));
        Status active = statusRepository.findByNameIgnoreCase("active")
                .orElseThrow();
        roleRepository.save(new Role(0,"user"));

        User user = builder.setName("testName")
                .setPassword("testPassword")
                .setRoles(List.of(roleRepository.findRoleByName("user").orElseThrow()))
                .setStatus(active)
                .build();
        repository.save(user);
    }

    @Test
    void setDelete() {
        init();
        Status deleted = statusRepository.findByNameIgnoreCase("deleted").orElseThrow();
        User user = repository.findUserByUsername("testName").orElseThrow();
        repository.setDelete(user.getId(),deleted);
        assertEquals(deleted,repository.getById(user.getId()).getStatus());
    }

    @Test
    void findUsersByOrdersEmpty() {
        init();
        assertNull(repository.findUsersByOrdersEmpty().stream().findAny().orElseThrow().getOrders());
    }

    @Test
    void findByStatus() {
        init();
        Status active = statusRepository.findByNameIgnoreCase("active").orElseThrow();
        User user = repository.findByStatus(active).stream().findAny().orElseThrow();
        assertEquals(active,user.getStatus());
    }

    @Test
    void findUserByUsername() {
        init();
        String expected = "testName";
        assertEquals(expected,repository.findUserByUsername("testName").orElseThrow().getUsername());
    }
}