package com.thoughtworks.blockchain.statementloader.loader;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoadingServiceFactory {

    private Map<String, LoadingService> loadingServices;

    @Autowired
    public LoadingServiceFactory(ListableBeanFactory listableBeanFactory) {
        loadingServices = listableBeanFactory.getBeansOfType(LoadingService.class);
    }

    public LoadingService getByBeanName(String beanName) {
        return loadingServices.get(beanName);
    }
}
