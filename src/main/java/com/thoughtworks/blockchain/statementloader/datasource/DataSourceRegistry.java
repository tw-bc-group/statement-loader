package com.thoughtworks.blockchain.statementloader.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DataSourceRegistry {

    private final ApplicationContext context;

    @Autowired
    public DataSourceRegistry(ApplicationContext context) {
        this.context = context;
    }

    public DataSource getByName(String name) {
        return context.getBean(name, DataSource.class);
    }
}
