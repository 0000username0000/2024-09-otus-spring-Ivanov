package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
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
                            "-----------------------------------",
                            String.format("Invalid value. Please enter a number between %s and %s.",
                                    1, question.answers().size()));
            isAnswerValid = question.answers().get(answerNumber - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
