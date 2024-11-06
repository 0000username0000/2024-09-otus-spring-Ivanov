package ru.otus.hw.config.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.TestRunnerService;

@Component
public class AppRunner implements ApplicationRunner {

    private final TestRunnerService testRunnerService;

    public AppRunner(@Autowired TestRunnerService testRunnerService) {
        this.testRunnerService = testRunnerService;
    }

    @Override
    public void run(ApplicationArguments args) {
        testRunnerService.run();
    }
}
