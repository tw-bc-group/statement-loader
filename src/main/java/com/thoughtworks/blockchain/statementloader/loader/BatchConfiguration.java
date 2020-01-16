package com.thoughtworks.blockchain.statementloader.loader;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Bean
    public JobRepositoryFactoryBean jobRepositoryFactoryBean(@Qualifier("LoaderDataSource") DataSource dataSource,
                                                             @Autowired PlatformTransactionManager transactionManager) {
        final JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDatabaseType(DatabaseType.MYSQL.name());
        factoryBean.setDataSource(dataSource);
        factoryBean.setTransactionManager(transactionManager);
        return factoryBean;
    }
}
