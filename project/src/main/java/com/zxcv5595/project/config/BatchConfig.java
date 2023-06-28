package com.zxcv5595.project.config;

import com.zxcv5595.project.batch.KafkaMessageWriter;
import com.zxcv5595.project.batch.ProjectProcessor;
import com.zxcv5595.project.batch.ProjectReader;
import com.zxcv5595.project.domain.Project;
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


}
