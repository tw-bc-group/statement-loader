package com.thoughtworks.blockchain.statementloader.loader.mysql;

import com.google.gson.JsonObject;
import com.thoughtworks.blockchain.statementloader.datasource.OriginDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
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
public class MysqlLoaderJobConfiguration {

    private static final Resource outputResource = new FileSystemResource("output/outputData.txt");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OriginDataSourceService originDataSourceService;
    private static final String QUERY_FORMAT = "SELECT *\n" +
            "FROM %s p\n" +
            "WHERE UNIX_TIMESTAMP(p.update_time) * 1000 >= %d\n" +
            "  AND UNIX_TIMESTAMP(p.update_time) * 1000 < %d;";

    @Autowired
    public MysqlLoaderJobConfiguration(JobBuilderFactory jobBuilderFactory,
                                       StepBuilderFactory stepBuilderFactory,
                                       OriginDataSourceService originDataSourceService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.originDataSourceService = originDataSourceService;
    }

    @Bean(name = "MysqlReader")
    @JobScope
    public JdbcCursorItemReader<JsonObject> mysqlReader(
            @Value("#{jobParameters['startTime']}") Long startTime,
            @Value("#{jobParameters['endTime']}") Long endTime,
            @Value("#{jobParameters['dataSourceName']}") String dataSourceName,
            @Value("#{jobParameters['tableName']}") String tableName) {

        JdbcCursorItemReader<JsonObject> mysqlReader = new JdbcCursorItemReader<>();
        mysqlReader.setDataSource(originDataSourceService.getByBeanName(dataSourceName));
        mysqlReader.setSql(String.format(QUERY_FORMAT, tableName, startTime, endTime));
        mysqlReader.setRowMapper(new MysqlRowMapper());

        return mysqlReader;
    }

    @Bean(name = "MysqlWriter")
    @JobScope
    public FlatFileItemWriter<JsonObject> writer() {
        final FlatFileItemWriter<JsonObject> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);
        writer.setLineAggregator(new DelimitedLineAggregator<>());
        return writer;
    }

    @Bean(name = "MysqlStep")
    public Step step(@Qualifier("MysqlReader") ItemReader<JsonObject> reader,
                     @Qualifier("MysqlWriter") ItemWriter<JsonObject> writer) {
        return stepBuilderFactory.get("mysqlLoadingStep")
                .<JsonObject, JsonObject>chunk(5)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean(name = "MysqlJob")
    public Job job(JobCompletionNotificationListener listener,
                   @Qualifier("MysqlStep") Step step) {
        return jobBuilderFactory.get("mysqlLoadingJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }
}
