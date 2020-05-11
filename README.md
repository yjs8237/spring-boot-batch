# spring-boot-batch

> Spring - boot - batch Job 실행 demo Application

2개의 Task Job 을 생성하여 실행시켜본다. 

``` jobBuilderFactory ``` 의 step 인자를 Step1 , Step2 로 구분하여 

각각의 Step 에서 로그를 찍어 각 Job 마다 실행되는 시점을 확인해보자

> build.gradle

```java
plugins {
    id 'org.springframework.boot' version '2.2.7.RELEASE'
    id 'io.spring.dependency-management' version '1.0.9.RELEASE'
    id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '1.8'

configurations {
    developmentOnly
    runtimeClasspath {
        extendsFrom developmentOnly
    }
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-batch'
    implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-quartz'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.mariadb.jdbc:mariadb-java-client:2.6.0'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'com.h2database:h2'
    runtimeOnly 'mysql:mysql-connector-java'
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
    testImplementation 'org.springframework.batch:spring-batch-test'
}

test {
    useJUnitPlatform()
}

```

- BatchConfig.java
> Spring batch 설정 관련 클래스
> com.example.springbatch.batch.BatchConfig.java

```java
package com.example.springbatch.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step stepOne() {
        return stepBuilderFactory.get("stepOne")
                .tasklet(new MyTaskOne())
                .build();
    }
    @Bean
    public Step stepTwo() {
        return stepBuilderFactory.get("stepTwo")
                .tasklet(new MyTaskTwo())
                .build();
    }

    @Bean
    public Job demoJob() {
        return jobBuilderFactory.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                .next(stepTwo())
                .build();
    }

}

```

- MyTaskOne.java
> 첫번째 Task 클래스
> com.example.springbatch.batch.MyTaskOne.java

```java
package com.example.springbatch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class MyTaskOne implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("MyTaskOne start");

        log.info("MyTaskOne end");
        return RepeatStatus.FINISHED;
    }
}

```

- MyTaskTwo.java
> 두번째 Task 클래스
> com.example.springbatch.batch.MyTaskTwo.java

```java
package com.example.springbatch.batch;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class MyTaskTwo implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("MyTaskTwo start");

        log.info("MyTaskTwo end");
        return RepeatStatus.FINISHED;
    }
}
```

이제 스프링부트 어플리케이션이 최초 구동될때 batch job 이 실행 되도록 CommandLineRunner 인터페이스를 구현한 클래스에서

job 을 실행시켜보자~

- BatchRunner.java
> 최초 구동시 호출되는 메소드가 존재하는 클래스
> com.example.springbatch.BatchRunner.java

```java
package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchRunner implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Override
    public void run(String... args) throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("jobID" , String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job , params);
    }
}

```


