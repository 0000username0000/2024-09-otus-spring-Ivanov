package ru.otus.hw.config.batch;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.config.batch.processor.AuthorProcessor;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.mongo.AuthorDocument;

@Configuration
public class AuthorBatchConfig {

    @Bean
    public JpaPagingItemReader<Author> authorItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Author>()
                .entityManagerFactory(entityManagerFactory)
                .name("authorItemReader")
                .queryString("SELECT a FROM Author a")
                .pageSize(10)
                .build();
    }

    @Bean
    public MongoItemWriter<AuthorDocument> authorMongoWriter(MongoTemplate mongoTemplate) {
        MongoItemWriter<AuthorDocument> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("authorCollection");
        return writer;
    }

    @Bean
    public Step authorStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                           JpaPagingItemReader<Author> reader,
                           MongoItemWriter<AuthorDocument> writer) {
        return new StepBuilder("authorStep", jobRepository)
                .<Author, AuthorDocument>chunk(10, transactionManager)
                .reader(reader)
                .processor(authorProcessor())
                .writer(writer)
                .build();
    }

    @Bean
    public AuthorProcessor authorProcessor() {
        return new AuthorProcessor();
    }
}
