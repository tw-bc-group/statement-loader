package com.thoughtworks.blockchain.statementloader.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class LoaderDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties loaderDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "LoaderDataSource")
    @Primary
    public DataSource loaderDataSource() {
        return loaderDataSourceProperties().initializeDataSourceBuilder().build();
    }

}
