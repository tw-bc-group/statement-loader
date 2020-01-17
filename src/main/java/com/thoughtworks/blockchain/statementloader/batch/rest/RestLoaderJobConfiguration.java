package com.thoughtworks.blockchain.statementloader.batch.rest;

import com.google.gson.JsonObject;
import com.thoughtworks.blockchain.statementloader.batch.mysql.JobCompletionNotificationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Slf4j
@Configuration
public class RestLoaderJobConfiguration {

    private static final Resource outputResource = new FileSystemResource("output/outputDataRest.txt");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public RestLoaderJobConfiguration(JobBuilderFactory jobBuilderFactory,
                                      StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(name = "RestWriter")
    @JobScope
    public FlatFileItemWriter<JsonObject> writer() {
        final FlatFileItemWriter<JsonObject> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);
        writer.setLineAggregator(new DelimitedLineAggregator<>());
        return writer;
    }

    @Bean(name = "RestReader")
    @JobScope
    public RestItemReader reader(@Value("${loader.bridge.url}") String apiUrl) {
        return new RestItemReader(apiUrl);
    }

    @Bean(name = "RestStep")
    public Step step(@Qualifier("RestReader") ItemReader<JsonObject> reader,
                     @Qualifier("RestWriter") ItemWriter<JsonObject> writer) {
        return stepBuilderFactory.get("restLoadingStep")
                .<JsonObject, JsonObject>chunk(5)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean(name = "RestJob")
    public Job job(JobCompletionNotificationListener listener,
                   @Qualifier("RestStep") Step step) {
        return jobBuilderFactory.get("restLoadingJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }
}
