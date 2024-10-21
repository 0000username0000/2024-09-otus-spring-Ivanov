package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("application.properties")
@Configuration
public class AppConfig {

//    @Bean
//    public AppProperties appProperties() {
//        return new AppProperties();
//    }
}
