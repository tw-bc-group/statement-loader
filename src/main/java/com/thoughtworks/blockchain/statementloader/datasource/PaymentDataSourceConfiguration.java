package com.thoughtworks.blockchain.statementloader.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class PaymentDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("loader.payment.datasource")
    public DataSourceProperties paymentDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "PaymentDataSource")
    public DataSource loaderDataSource() {
        return paymentDataSourceProperties().initializeDataSourceBuilder().build();
    }
}
