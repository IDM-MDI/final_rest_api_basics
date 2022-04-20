package com.epam.esm.task.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@Profile("test")
public class SpringJdbcTestConfig {
    @Bean("dataSourceTest")
    public DataSource getTestDataSource() {
        return new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.HSQL).
                addScript("schema.sql").
                addScript("fillData.sql").
                build();
    }

    @Bean("jdbcTemplateTest")
    public JdbcTemplate getJdbcTemplateTest(@Autowired DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSourceTransactionManager getDataSourceTransactionManager(@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
