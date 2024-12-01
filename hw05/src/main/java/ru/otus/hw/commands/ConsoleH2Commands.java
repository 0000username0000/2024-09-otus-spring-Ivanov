package ru.otus.hw.commands;

import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

@ShellComponent
public class ConsoleH2Commands {

    @ShellMethod(value = "Run console", key = "run")
    private void runConsoleH2() throws SQLException {
        Console.main();
    }

}
