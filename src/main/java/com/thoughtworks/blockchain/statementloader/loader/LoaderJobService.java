package com.thoughtworks.blockchain.statementloader.loader;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoaderJobService {

    private final Map<String, Job> jobs;

    @Autowired
    public LoaderJobService(ListableBeanFactory beanFactory) {
        jobs = beanFactory.getBeansOfType(Job.class);
    }

    public Job getByBeanName(String beanName) {
        return jobs.get(beanName);
    }
}
