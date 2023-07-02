package com.zxcv5595.project.batch;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSyncScheduler {

    private final JobLauncher jobLauncher;
    private final Job dataSyncJob;
    private final Job failureProjectJob;
    private final ProjectReader projectReader;
    private final ProjectStatusReader projectStatusReader;

//    @Scheduled(cron = "*/10 * * * * *")// 10초 마다 테스트
    @Scheduled(cron = "0 0 23 * * *")// 매 오후 11시 마다 dataSync
    public void performDataSyncJob()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        projectReader.setExecuted(false); //재실행을 위한, 실행완료된 Reader 초기화
        projectStatusReader.setExecuted(false);

        jobLauncher.run(dataSyncJob, jobParameters);
        jobLauncher.run(failureProjectJob, jobParameters);
    }
}
