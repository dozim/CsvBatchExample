package com.example.csvbatchexample.batch.web;

import com.example.csvbatchexample.batch.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobExplorer jobExplorer;
    private final JobService jobService;

    @GetMapping("/names")
    public List<String> getJobNames() {
        return this.jobExplorer.getJobNames();
    }

    @GetMapping
    public List<JobExecution> getJobExecutions(String jobName) {
        JobInstance lastJobInstance = this.jobExplorer.getLastJobInstance(jobName);
        return this.jobExplorer.getJobExecutions(lastJobInstance);
    }

    @PostMapping("/read/{filename}")
    public void runReadJob(@PathVariable String filename) {
        this.jobService.runReadJob(filename);
    }

    @PostMapping("/write/{filename}")
    public void runWriteJob(@PathVariable String filename) {
        this.jobService.runWriteJob(filename);
    }
}
