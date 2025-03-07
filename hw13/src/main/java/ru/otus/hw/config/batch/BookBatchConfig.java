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
import ru.otus.hw.config.batch.processor.BookProcessor;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookDocument;

@Configuration
public class BookBatchConfig {

    @Bean
    public JpaPagingItemReader<Book> bookItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Book>()
                .entityManagerFactory(entityManagerFactory)
                .name("bookItemReader")
                .queryString("SELECT b FROM Book b")
                .pageSize(10)
                .build();
    }

    @Bean
    public MongoItemWriter<BookDocument> bookMongoWriter(MongoTemplate mongoTemplate) {
        MongoItemWriter<BookDocument> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("bookCollection");
        return writer;
    }

    @Bean
    public Step bookStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                         JpaPagingItemReader<Book> reader,
                         MongoItemWriter<BookDocument> writer) {
        return new StepBuilder("bookStep", jobRepository)
                .<Book, BookDocument>chunk(10, transactionManager)
                .reader(reader)
                .processor(bookProcessor())
                .writer(writer)
                .build();
    }

    @Bean
    public BookProcessor bookProcessor() {
        return new BookProcessor();
    }
}