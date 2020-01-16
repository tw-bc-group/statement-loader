package com.thoughtworks.blockchain.statementloader.loader.rest;

import com.google.gson.JsonObject;
import com.thoughtworks.blockchain.statementloader.loader.mysql.JobCompletionNotificationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Slf4j
@Configuration
@EnableBatchProcessing
public class RestLoaderConfiguration {

    private static final Resource outputResource = new FileSystemResource("output/outputDataRest.txt");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public RestLoaderConfiguration(JobBuilderFactory jobBuilderFactory,
                                   StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    // set data source for JobRepository to record job metadata
//    @Override
//    public void setDataSource(@Qualifier("LoaderDataSource") DataSource dataSource) {
//        super.setDataSource(dataSource);
//    }
//
//    @Bean(name = "LoaderDataSource")
//    public BatchDataSourceInitializer batchDataSourceInitializer(@Qualifier("LoaderDataSource") DataSource dataSource,
//                                                                 ResourceLoader resourceLoader) {
//        return new BatchDataSourceInitializer(dataSource, resourceLoader, new BatchProperties());
//    }

    @Bean(name = "RestReader")
    @JobScope
    public RestJsonReader<JsonObject> restReader() {
        return new RestJsonReader<>();
    }

    @Bean(name = "RestWriter")
    @JobScope
    public FlatFileItemWriter<JsonObject> writer() {
        final FlatFileItemWriter<JsonObject> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);
        writer.setLineAggregator(new DelimitedLineAggregator<>());
        return writer;
    }

    @Bean(name = "RestStep")
    public Step step(@Qualifier("RestReader") ItemReader<JsonObject> reader,
                     @Qualifier("RestWriter") ItemWriter<JsonObject> writer) {
        return stepBuilderFactory.get("loadingDataStep")
                .<JsonObject, JsonObject>chunk(5)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean(name = "RestJob")
    public Job job(JobCompletionNotificationListener listener,
                   @Qualifier("RestStep") Step step) {
        return jobBuilderFactory.get("loadingDataJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }
}
