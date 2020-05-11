package com.example.springbatch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("/")
    @ResponseBody
    public String home() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("jobID" , String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job , params);
        return "home";
    }
}
