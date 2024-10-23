package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.TestRunnerService;


@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(Application.class);
//        var testRunnerService = context.getBean(TestRunnerService.class);
//        testRunnerService.run();
    }

}