package com.epam.esm.config;

import com.epam.esm.generator.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
@EnableJpaRepositories
public class WebApplication {

    private final Environment env;
    private final DataGenerator generator;

    @Autowired
    public WebApplication(DataGenerator generator, Environment env) {
        this.generator = generator;
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillRandomDataToDatabase() {
        if(Arrays.stream(env.getActiveProfiles()).toList().contains("test")) {
            return;
        }
        generator.fillRandomData();
    }
}
