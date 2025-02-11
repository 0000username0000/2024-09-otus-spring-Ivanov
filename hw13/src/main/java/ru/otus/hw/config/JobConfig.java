package ru.otus.hw.config;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.models.Genre;

public class JobConfig {

//    @Bean
//    public JpaPagingItemReader<Genre> genreItemReader() {
//        JpaPagingItemReader<Genre> reader = new JpaPagingItemReader<>();
//        reader.setEntityManagerFactory(entityManagerFactory);
//        reader.setQueryString("SELECT g FROM Genre g");
//        reader.setPageSize(10);
//        return reader;
//    }
}
