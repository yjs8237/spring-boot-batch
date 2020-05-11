package com.example.springbatch.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;

public class TestJob implements Job {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isRestartable() {
        return false;
    }

    @Override
    public void execute(JobExecution execution) {

    }

    @Override
    public JobParametersIncrementer getJobParametersIncrementer() {
        return null;
    }

    @Override
    public JobParametersValidator getJobParametersValidator() {
        return null;
    }
}
