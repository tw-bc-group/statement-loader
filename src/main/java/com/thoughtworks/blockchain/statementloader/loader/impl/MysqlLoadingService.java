package com.thoughtworks.blockchain.statementloader.loader.impl;

import com.thoughtworks.blockchain.statementloader.loader.LoadingService;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service("Mysql")
public class MysqlLoadingService implements LoadingService {

    private final JobLauncher jobLauncher;
    private final Job job;

    @Autowired
    public MysqlLoadingService(JobLauncher jobLauncher,
                               @Qualifier("MysqlJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Override
    public InputStreamResource loadingData(Long startTimestamp, Long endTimestamp) {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("dataSourceName", "PaymentDataSource")
                .addString("tableName", "payment_records")
                .addLong("startTime", startTimestamp)
                .addLong("endTime", endTimestamp)
                .addLong("executeTime", System.currentTimeMillis())
                .toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            final FileInputStream fileInputStream = FileUtils.openInputStream(new File("output/outputData.txt"));
            return new InputStreamResource(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
