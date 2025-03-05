package ru.otus.hw.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;

    public BatchConfig(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public SimpleFlow splitFlow(TaskExecutor taskExecutor, Step genreStep, Step authorStep) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor)
                .add(new FlowBuilder<SimpleFlow>("genreFlow").start(genreStep).build(),
                        new FlowBuilder<SimpleFlow>("authorFlow").start(authorStep).build())
                .build();
    }

    @Bean
    public Job migrationJob(TaskExecutor taskExecutor, SimpleFlow splitFlow, Step bookStep) {
        return new JobBuilder("migrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow)
                .next(bookStep)
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
