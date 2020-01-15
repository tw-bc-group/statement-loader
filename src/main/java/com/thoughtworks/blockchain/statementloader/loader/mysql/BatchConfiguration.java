package com.thoughtworks.blockchain.statementloader.loader.mysql;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private static final String QUERY_FIND_ACCOUNT_RECORDS = "SELECT * FROM account_records";
    private final Resource outputResource = new FileSystemResource("output/outputData.txt");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource originDataSource;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              @Qualifier("AccountCenterDataSource") DataSource originDataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.originDataSource = originDataSource;
    }

    // prevent error: Table 'statement.BATCH_JOB_INSTANCE' doesn't exist
    @Override
    @Autowired
    public void setDataSource(@Qualifier("LoaderDataSource") DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    @Bean
    public BatchDataSourceInitializer batchDataSourceInitializer(@Qualifier("LoaderDataSource") DataSource dataSource, ResourceLoader resourceLoader) {
        return new BatchDataSourceInitializer(dataSource, resourceLoader, new BatchProperties());
    }

    @Bean
//    @StepScope
    public ItemReader<JsonObject> reader() {
        JdbcCursorItemReader<JsonObject> mysqlReader = new JdbcCursorItemReader<>();
        mysqlReader.setDataSource(originDataSource);
        mysqlReader.setSql(QUERY_FIND_ACCOUNT_RECORDS);
        mysqlReader.setRowMapper(new MysqlRowMapper());

        return mysqlReader;
    }

    @Bean
    public ItemWriter<JsonObject> writer() {
        final FlatFileItemWriter<JsonObject> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);
        writer.setLineAggregator(new DelimitedLineAggregator<>());
        return writer;

//        return items -> items.forEach(System.outputStream::println);
    }

    @Bean
    public ItemProcessor<JsonObject, JsonObject> processor() {
        return item -> item;
    }

    @Bean
    public Step step(ItemReader<JsonObject> reader) {
        return stepBuilderFactory.get("step")
                .<JsonObject, JsonObject>chunk(5)
                .reader(reader)
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(JobCompletionNotificationListener listener, Step step) {
        return jobBuilderFactory.get("importAccountRecordsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }
}
