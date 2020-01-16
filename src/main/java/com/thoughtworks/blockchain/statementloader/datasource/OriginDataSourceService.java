package com.thoughtworks.blockchain.statementloader.datasource;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Service
public class OriginDataSourceService {

    private final Map<String, DataSource> dataSources;

    @Autowired
    public OriginDataSourceService(ListableBeanFactory beanFactory) {
        dataSources = beanFactory.getBeansOfType(DataSource.class);
    }

    public DataSource getByBeanName(String name) {
        return dataSources.get(name);
    }
}
