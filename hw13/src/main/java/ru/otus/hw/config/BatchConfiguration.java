package ru.otus.hw.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.mongo.GenreDocument;

@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final PlatformTransactionManager platformTransactionManager;

    public BatchConfiguration(JobRepository jobRepository,
                              EntityManagerFactory entityManagerFactory,
                              PlatformTransactionManager platformTransactionManager) {
        this.jobRepository = jobRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public JpaPagingItemReader<Genre> genreItemReader() {
        return new JpaPagingItemReaderBuilder<Genre>()
                .entityManagerFactory(entityManagerFactory)
                .name("genreItemReader")
                .queryString("SELECT g FROM Genre g")
                .pageSize(10)
                .build();
    }

    @Bean
    public FlatFileItemWriter<GenreDocument> genreFileWriter() {
        return new FlatFileItemWriterBuilder<GenreDocument>()
                .name("genreFileWriter")
                .resource(new FileSystemResource("src/main/resources/output/genres.csv"))
                .lineAggregator(new DelimitedLineAggregator<GenreDocument>() {{
                    setDelimiter(",");
                    setFieldExtractor(genreDocument -> new Object[]{genreDocument.getId(), genreDocument.getName()});
                }})
                .build();
    }

    @Bean
    public Step genreStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("genreStep", jobRepository)
                .<Genre, GenreDocument>chunk(10, platformTransactionManager)
                .reader(genreItemReader())
                .processor(genreProcessor())
                .writer(genreFileWriter())
                .build();
    }

    @Bean
    public Job genreJob() {
        return new JobBuilder("genreJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(genreStep(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public GenreProcessor genreProcessor() {
        return new GenreProcessor();
    }
}
