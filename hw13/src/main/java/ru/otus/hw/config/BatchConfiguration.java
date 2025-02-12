package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.config.batch.processor.AuthorProcessor;
import ru.otus.hw.config.batch.processor.BookProcessor;
import ru.otus.hw.config.batch.processor.GenreProcessor;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookDocument;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.GenreDocument;


//@Configuration
public class BatchConfiguration {
//
//    private final JobRepository jobRepository;
//    private final EntityManagerFactory entityManagerFactory;
//    private final PlatformTransactionManager platformTransactionManager;
//    private final MongoTemplate mongoTemplate;
//
//    public BatchConfiguration(JobRepository jobRepository,
//                              EntityManagerFactory entityManagerFactory,
//                              PlatformTransactionManager platformTransactionManager,
//                              MongoTemplate mongoTemplate) {
//        this.jobRepository = jobRepository;
//        this.entityManagerFactory = entityManagerFactory;
//        this.platformTransactionManager = platformTransactionManager;
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    // ============================ Genre Migration =============================
//
//    @Bean
//    public JpaPagingItemReader<Genre> genreItemReader() {
//        return new JpaPagingItemReaderBuilder<Genre>()
//                .entityManagerFactory(entityManagerFactory)
//                .name("genreItemReader")
//                .queryString("SELECT g FROM Genre g")
//                .pageSize(10)
//                .build();
//    }
//
//    @Bean
//    public FlatFileItemWriter<GenreDocument> genreFileWriter() {
//        return new FlatFileItemWriterBuilder<GenreDocument>()
//                .name("genreFileWriter")
//                .resource(new FileSystemResource("src/main/resources/output/genres.csv"))
//                .lineAggregator(new DelimitedLineAggregator<GenreDocument>() {{
//                    setDelimiter(",");
//                    setFieldExtractor(genreDocument -> new Object[]{genreDocument.getId(), genreDocument.getName()});
//                }})
//                .build();
//    }
//
//    @Bean
//    public MongoItemWriter<GenreDocument> genreMongoWriter() {
//        MongoItemWriter<GenreDocument> writer = new MongoItemWriter<>();
//        writer.setTemplate(mongoTemplate);
//        writer.setCollection("genreCollection");
//        return writer;
//    }
//
//    @Bean
//    public Step genreStep() {
//        return new StepBuilder("genreStep", jobRepository)
//                .<Genre, GenreDocument>chunk(10, platformTransactionManager)
//                .reader(genreItemReader())
//                .processor(genreProcessor())
//                .writer(genreMongoWriter())
//                .writer(genreFileWriter())
//                .build();
//    }
//
//    // ============================ Author Migration =============================
//
//    @Bean
//    public JpaPagingItemReader<Author> authorItemReader() {
//        return new JpaPagingItemReaderBuilder<Author>()
//                .entityManagerFactory(entityManagerFactory)
//                .name("authorItemReader")
//                .queryString("SELECT a FROM Author a")
//                .pageSize(10)
//                .build();
//    }
//
//    @Bean
//    public FlatFileItemWriter<AuthorDocument> authorFileWriter() {
//        return new FlatFileItemWriterBuilder<AuthorDocument>()
//                .name("authorFileWriter")
//                .resource(new FileSystemResource("src/main/resources/output/authors.csv"))
//                .lineAggregator(new DelimitedLineAggregator<AuthorDocument>() {{
//                    setDelimiter(",");
//                    setFieldExtractor(authorDocument -> new Object[]{authorDocument.getId(), authorDocument.getFullName()});
//                }})
//                .build();
//    }
//
//    @Bean
//    public MongoItemWriter<AuthorDocument> authorMongoWriter() {
//        MongoItemWriter<AuthorDocument> writer = new MongoItemWriter<>();
//        writer.setTemplate(mongoTemplate);
//        writer.setCollection("authorCollection");
//        return writer;
//    }
//
//    @Bean
//    public Step authorStep() {
//        return new StepBuilder("authorStep", jobRepository)
//                .<Author, AuthorDocument>chunk(10, platformTransactionManager)
//                .reader(authorItemReader())
//                .processor(authorProcessor())
//                .writer(authorMongoWriter())
//                .writer(authorFileWriter())
//                .build();
//    }
//
//    // ============================ Book Migration =============================
//
//    @Bean
//    public JpaPagingItemReader<Book> bookItemReader() {
//        return new JpaPagingItemReaderBuilder<Book>()
//                .entityManagerFactory(entityManagerFactory)
//                .name("bookItemReader")
//                .queryString("SELECT b FROM Book b")
//                .pageSize(10)
//                .build();
//    }
//
//    @Bean
//    public FlatFileItemWriter<BookDocument> bookFileWriter() {
//        return new FlatFileItemWriterBuilder<BookDocument>()
//                .name("bookFileWriter")
//                .resource(new FileSystemResource("src/main/resources/output/books.csv"))
//                .lineAggregator(new DelimitedLineAggregator<BookDocument>() {{
//                    setDelimiter(",");
//                    setFieldExtractor(bookDocument ->
//                            new Object[]{bookDocument.getId(),
//                                    bookDocument.getTitle(),
//                                    bookDocument.getAuthor(),
//                                    bookDocument.getGenres()});
//                }})
//                .build();
//    }
//
//    @Bean
//    public MongoItemWriter<BookDocument> bookMongoWriter() {
//        MongoItemWriter<BookDocument> writer = new MongoItemWriter<>();
//        writer.setTemplate(mongoTemplate);
//        writer.setCollection("bookCollection");
//        return writer;
//    }
//
//    @Bean
//    public Step bookStep() {
//        return new StepBuilder("bookStep", jobRepository)
//                .<Book, BookDocument>chunk(10, platformTransactionManager)
//                .reader(bookItemReader())
//                .processor(bookProcessor())
//                .writer(bookMongoWriter())
//                .writer(bookFileWriter())
//                .build();
//    }
//
//    // ============================ Parallel Job Execution =============================
//
//    @Bean
//    public SimpleFlow splitFlow() {
//        return new FlowBuilder<SimpleFlow>("genreFlow").split(taskExecutor()).add(
//                new FlowBuilder<SimpleFlow>("flow1")
//                .start(genreStep())
//                .build(), new FlowBuilder<SimpleFlow>("flow2")
//                .start(authorStep())
//                .build()).build();
//    }
//
//
//    @Bean
//    public Job migrationJob() {
//        return new JobBuilder("migrationJob", jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .start(splitFlow())
//                .next(bookStep())
//                .end()
//                .build();
//    }
//
//    @Bean
//    public GenreProcessor genreProcessor() {
//        return new GenreProcessor();
//    }
//
//    @Bean
//    public AuthorProcessor authorProcessor() {
//        return new AuthorProcessor();
//    }
//
//    @Bean
//    public BookProcessor bookProcessor() {
//        return new BookProcessor();
//    }
//
//    @Bean
//    public TaskExecutor taskExecutor() {
//        return new SimpleAsyncTaskExecutor("spring_batch");
//    }
}
