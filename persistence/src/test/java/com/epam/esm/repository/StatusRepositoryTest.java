package com.epam.esm.repository;

import com.epam.esm.config.PersistenceConfig;
import com.epam.esm.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class StatusRepositoryTest {

    @Autowired
    private StatusRepository repository;

    @Test
    void findByNameIgnoreCase() {
        String expected = "active";
        repository.saveAll(List.of(new Status(null, expected),new Status(null,"deleted")));
        Status actual = repository.findByNameIgnoreCase(expected).orElseThrow();
        assertEquals(expected,actual.getName());
    }
}