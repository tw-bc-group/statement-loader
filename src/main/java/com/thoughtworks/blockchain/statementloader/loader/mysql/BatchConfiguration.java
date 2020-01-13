package com.thoughtworks.blockchain.statementloader.loader.mysql;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration extends DefaultBatchConfigurer {

    private static final String QUERY_FIND_ACCOUNT_RECORDS = "SELECT * FROM account_records";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              @Qualifier("Mysql") DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    // prevent error: Table 'statement.BATCH_JOB_INSTANCE' doesn't exist
    @Override
    public void setDataSource(DataSource dataSource) {

    }

    @Bean
//    @StepScope
    public ItemReader<JsonObject> reader() {
        JdbcCursorItemReader<JsonObject> mysqlReader = new JdbcCursorItemReader<>();
        mysqlReader.setDataSource(dataSource);
        mysqlReader.setSql(QUERY_FIND_ACCOUNT_RECORDS);
        mysqlReader.setRowMapper(new MysqlRowMapper());

        return mysqlReader;
    }

    @Bean
    public ItemWriter<JsonObject> writer() {
        return items -> items.forEach(System.out::println);
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
                .listener(new ChunkListener() {
                    @Override
                    public void beforeChunk(ChunkContext context) {
                        System.out.println("before chunk");
                    }

                    @Override
                    public void afterChunk(ChunkContext context) {
                        System.out.println("after chunk");
                        System.out.println("status: " + context.getStepContext());
                    }

                    @Override
                    public void afterChunkError(ChunkContext context) {

                    }
                })
                .build();
    }

    @Bean
    public Job job(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importAccountRecordsJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }
}
