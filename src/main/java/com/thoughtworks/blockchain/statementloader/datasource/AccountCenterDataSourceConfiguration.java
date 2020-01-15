package com.thoughtworks.blockchain.statementloader.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AccountCenterDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("loader.account-center.datasource")
    public DataSourceProperties accountCenterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "AccountCenterDataSource")
    public DataSource loaderDataSource() {
        return accountCenterDataSourceProperties().initializeDataSourceBuilder().build();
    }
}
