package com.thoughtworks.blockchain.statementloader.loader.mysql;

import com.google.gson.JsonObject;
import com.thoughtworks.blockchain.statementloader.datasource.DataSourceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    private static final Resource outputResource = new FileSystemResource("output/outputData.txt");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSourceRegistry dataSourceRegistry;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              DataSourceRegistry dataSourceRegistry) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSourceRegistry = dataSourceRegistry;
    }

    // set data source for JobRepository to record job metadata
    @Override
    public void setDataSource(@Qualifier("LoaderDataSource") DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Bean
    public BatchDataSourceInitializer batchDataSourceInitializer(@Qualifier("LoaderDataSource") DataSource dataSource,
                                                                 ResourceLoader resourceLoader) {
        return new BatchDataSourceInitializer(dataSource, resourceLoader, new BatchProperties());
    }

    @Bean(name = "MysqlReader")
    @JobScope
    public JdbcCursorItemReader<JsonObject> mysqlReader(
            @Value("#{jobParameters['startTime']}") Long startTime,
            @Value("#{jobParameters['endTime']}") Long endTime,
            @Value("#{jobParameters['dataSourceName']}") String dataSourceName,
            @Value("#{jobParameters['tableName']}") String tableName) {

        final String querySql = String.format("SELECT * FROM %s t WHERE t.timestamp >= %d AND t.timestamp < %d", tableName, startTime, endTime);

        JdbcCursorItemReader<JsonObject> mysqlReader = new JdbcCursorItemReader<>();
        mysqlReader.setDataSource(dataSourceRegistry.getByName(dataSourceName));
        mysqlReader.setSql(querySql);
        mysqlReader.setRowMapper(new MysqlRowMapper());

        return mysqlReader;
    }

    @Bean
    @JobScope
    public FlatFileItemWriter<JsonObject> writer() {
        final FlatFileItemWriter<JsonObject> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);
        writer.setLineAggregator(new DelimitedLineAggregator<>());
        return writer;
    }

    @Bean
    public Step step(@Qualifier("MysqlReader") ItemReader<JsonObject> reader) {
        return stepBuilderFactory.get("loadingDataStep")
                .<JsonObject, JsonObject>chunk(5)
                .reader(reader)
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(JobCompletionNotificationListener listener, Step step) {
        return jobBuilderFactory.get("loadingDataJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }
}
