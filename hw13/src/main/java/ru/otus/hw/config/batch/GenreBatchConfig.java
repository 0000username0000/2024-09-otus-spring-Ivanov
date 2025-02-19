package ru.otus.hw.config.batch;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.config.batch.processor.GenreProcessor;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.mongo.GenreDocument;

@Configuration
public class GenreBatchConfig {

    @Bean
    public JpaPagingItemReader<Genre> genreItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Genre>()
                .entityManagerFactory(entityManagerFactory)
                .name("genreItemReader")
                .queryString("SELECT g FROM Genre g")
                .pageSize(10)
                .build();
    }

    @Bean
    public MongoItemWriter<GenreDocument> genreMongoWriter(MongoTemplate mongoTemplate) {
        MongoItemWriter<GenreDocument> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("genreCollection");
        return writer;
    }

    @Bean
    public Step genreStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          JpaPagingItemReader<Genre> reader,
                          MongoItemWriter<GenreDocument> writer) {
        return new StepBuilder("genreStep", jobRepository)
                .<Genre, GenreDocument>chunk(10, transactionManager)
                .reader(reader)
                .processor(genreProcessor())
                .writer(writer)
                .build();
    }

    @Bean
    public GenreProcessor genreProcessor() {
        return new GenreProcessor();
    }
}
