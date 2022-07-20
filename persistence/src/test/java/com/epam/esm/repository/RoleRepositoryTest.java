package com.epam.esm.repository;

import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Test
    void findRoleByName() {
        String expected = "user";
        repository.save(new Role(0,expected));
        Role actual = repository.findRoleByName(expected).orElseThrow();
        assertEquals(expected,actual.getName());
    }
}