package ru.otus.hw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "batch")
public class AppConfig {

    private String genreFilePath;

    private String authorFilePath;

    private String bookFilePath;
}
