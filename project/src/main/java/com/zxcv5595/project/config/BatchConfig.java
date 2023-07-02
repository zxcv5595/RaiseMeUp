package com.zxcv5595.project.config;

import com.zxcv5595.project.batch.KafkaMessageWriter;
import com.zxcv5595.project.batch.ProjectProcessor;
import com.zxcv5595.project.batch.ProjectReader;
import com.zxcv5595.project.batch.ProjectStatusProcessor;
import com.zxcv5595.project.batch.ProjectStatusReader;
import com.zxcv5595.project.batch.ProjectStatusWriter;
import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.FailureProjectMessage;
import com.zxcv5595.project.dto.KafkaMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import reactor.core.publisher.Flux;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@EnableConfigurationProperties(BatchProperties.class)
public class BatchConfig {


    @Bean
    public Job dataSyncJob(JobRepository jobRepository, Step dataSyncStep) {
        return new JobBuilder("dataSyncJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(dataSyncStep)
                .end()
                .build();
    }


    @Bean
    public Job failureProjectJob(JobRepository jobRepository, Step failureProjectStep) {
        return new JobBuilder("failureProjectJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(failureProjectStep)
                .end()
                .build();
    }
    @Bean
    public Step dataSyncStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ProjectReader projectReader,
            ProjectProcessor projectProcessor,
            KafkaMessageWriter kafkaMessageWriter
    ) {
        return new StepBuilder("dataSyncStep", jobRepository)
                .<List<Project>, Flux<KafkaMessage>>chunk(100, transactionManager)
                .reader(projectReader)
                .processor(projectProcessor)
                .writer(kafkaMessageWriter)
                .build();
    }

    @Bean
    public Step failureProjectStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ProjectStatusReader statusReader,
            ProjectStatusProcessor statusProcessor,
            ProjectStatusWriter statusWriter
    ) {
        return new StepBuilder("failureProjectStep", jobRepository)
                .<List<Project>, Flux<FailureProjectMessage>>chunk(100, transactionManager)
                .reader(statusReader)
                .processor(statusProcessor)
                .writer(statusWriter)
                .build();
    }

}
