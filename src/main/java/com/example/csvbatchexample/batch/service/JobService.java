package com.example.csvbatchexample.batch.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class JobService {

    private final JobLauncher jobLauncher;
    private final Job readJob;
    private final Job writeJob;
    private final Consumer<String> fileNameReaderConsumer;
    private final Consumer<String> fileNameWriterConsumer;

    public JobService(JobLauncher jobLauncher,
                      @Qualifier("readJob") Job readJob,
                      @Qualifier("writeJob") Job writeJob,
                      Consumer<String> fileNameReaderConsumer,
                      Consumer<String> fileNameWriterConsumer) {
        this.jobLauncher = jobLauncher;
        this.readJob = readJob;
        this.writeJob = writeJob;
        this.fileNameReaderConsumer = fileNameReaderConsumer;
        this.fileNameWriterConsumer = fileNameWriterConsumer;
    }

    public void runReadJob(String filename) {
        try {
            this.fileNameReaderConsumer.accept(filename);
            jobLauncher.run(readJob, new JobParameters());

        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    public void runWriteJob(String filename) {
        try {
            this.fileNameWriterConsumer.accept(filename);
            jobLauncher.run(writeJob, new JobParameters());

        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
