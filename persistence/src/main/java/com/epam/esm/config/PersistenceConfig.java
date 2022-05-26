package com.epam.esm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.epam.esm")
@EnableJpaRepositories(basePackages = "com.epam.esm")
@EnableJpaAuditing
public class PersistenceConfig {
}
