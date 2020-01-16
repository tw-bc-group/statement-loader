package com.thoughtworks.blockchain.statementloader.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
@RestController
@RequestMapping("/batch-data")
public class LoadingApi {

    private final Job job;
    private final Job restJob;
    private final JobLauncher jobLauncher;

    @Autowired
    public LoadingApi(@Qualifier("MysqlJob") Job job,
                      @Qualifier("RestJob") Job restJob,
                      JobLauncher jobLauncher) {
        this.job = job;
        this.restJob = restJob;
        this.jobLauncher = jobLauncher;
    }

    @GetMapping("/bridge")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StreamingResponseBody> loadBridgeData()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, FileNotFoundException {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addLong("executeTime", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(restJob, jobParameters);

        final FileInputStream file = new FileInputStream(new File("output/outputDataRest.txt"));

        StreamingResponseBody body = outputStream -> {
            int n;
            while ((n = file.available()) > 0) {
                byte[] buffer = new byte[n];
                final int result = file.read(buffer);
                if (result == -1) break;
                outputStream.write(buffer);
                outputStream.flush();
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StreamingResponseBody> loadAccountCenterData(@RequestParam("startTime") Long startTime,
                                                                       @RequestParam("endTime") Long endTime)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, FileNotFoundException {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("dataSourceName", "AccountCenterDataSource")
                .addString("tableName", "payment_records")
                .addLong("startTime", startTime)
                .addLong("endTime", endTime)
                .addLong("executeTime", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(job, jobParameters);

        final FileInputStream file = new FileInputStream(new File("output/outputData.txt"));

        StreamingResponseBody body = outputStream -> {
            int n;
            while ((n = file.available()) > 0) {
                byte[] buffer = new byte[n];
                final int result = file.read(buffer);
                if (result == -1) break;
                outputStream.write(buffer);
                outputStream.flush();
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
