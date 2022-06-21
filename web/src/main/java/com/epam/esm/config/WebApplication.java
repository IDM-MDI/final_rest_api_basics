package com.epam.esm.config;

import com.epam.esm.exception.RepositoryException;
import com.epam.esm.generator.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
@EnableJpaRepositories
public class WebApplication {

    private final DataGenerator generator;

    @Autowired
    public WebApplication(DataGenerator generator) {
        this.generator = generator;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillRandomDataToDatabase() throws RepositoryException {
        generator.fillRandomData();
    }
}
