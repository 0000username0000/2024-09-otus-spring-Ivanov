package ru.otus.hw.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;


@ShellComponent(value = "Application Events Command")
@RequiredArgsConstructor
public class CommandShell {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Start of testing", key = "st")
    public void start() {
        testRunnerService.run();
    }
}
