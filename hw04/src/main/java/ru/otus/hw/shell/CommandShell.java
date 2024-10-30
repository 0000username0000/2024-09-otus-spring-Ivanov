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

import java.util.List;
import java.util.Objects;

@ShellComponent(value = "Application Events Command")
@RequiredArgsConstructor
public class CommandShell {
    private List<Question> questions;

    private TestResult testResult;

    private final StreamsIOService ioService;

    private final QuestionDao questionDao;

    private final ResultService resultService;

    @ShellMethod(value = "Start of testing", key = "start")
    public String start() {
        testResult = new TestResult(new Student(ioService.readStringWithPrompt("Please input your first name"),
                ioService.readStringWithPrompt("Please input your last name")));
        questions = questionDao.findAll();
        return "Enter 'test' for continue";
    }

    @ShellMethod(value = "testing", key = "test")
    @ShellMethodAvailability(value = "isTestingAvailable")
    public String testing() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below");
        questions = questionDao.findAll();
        for (var question: questions) {
            var isAnswerValid = false;
            ioService.printLine("-----------------------------------\n" + question.text() + "\n");
            for (int i = 0; i < question.answers().size(); i++) {
                ioService.printLine((i + 1) + ". " + question.answers().get(i).text());
            }
            int answerNumber = ioService
                    .readIntForRangeWithPrompt(
                            1,
                            question.answers().size(),
                            "-----------------------------------\n",
                            "Invalid value. Please enter a number between {0} and {1}"
                    );
            isAnswerValid = question.answers().get(answerNumber - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return "Enter 'finish' for result";
    }

    @ShellMethod(value = "Finish test", key = "finish")
    @ShellMethodAvailability(value = "isFinishAvailable")
    public void finish() {
        resultService.showResult(testResult);
    }

    private Availability isTestingAvailable() {
        return Objects.nonNull(testResult) ? Availability.available()
                : Availability.unavailable(". Please enter 'start'");
    }

    private Availability isFinishAvailable() {
        return Objects.nonNull(questions) ? Availability.available()
                : Availability.unavailable(". Please enter 'testing'");
    }
}
