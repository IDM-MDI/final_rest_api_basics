package com.epam.esm.generator;

import com.epam.esm.config.PersistenceConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
@ContextConfiguration(classes= PersistenceConfig.class)
@ComponentScan(basePackages = "com.epam.esm")
class DataGeneratorTest {

    @Autowired
    private DataGenerator generator;

    @SneakyThrows
    @Test
    void fillRandomData() {
        System.out.println("12345");

    }
}