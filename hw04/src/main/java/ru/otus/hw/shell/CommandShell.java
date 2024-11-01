package ru.otus.hw.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StreamsIOService;
import ru.otus.hw.service.TestRunnerService;

import java.util.List;
import java.util.Objects;

@ShellComponent(value = "Application Events Command")
@RequiredArgsConstructor
public class CommandShell {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Start of testing", key = "st")
    public void start() {
        testRunnerService.run();
    }
}
