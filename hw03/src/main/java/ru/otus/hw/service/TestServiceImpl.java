package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        for (var question: questions) {
            var isAnswerValid = false;
            ioService.printLine("-----------------------------------\n" + question.text() + "\n");
            for (int i = 0; i < question.answers().size(); i++) {
                ioService.printLine((i + 1) + ". " + question.answers().get(i).text());
            }
            int answerNumber = ioService
                    .readIntForRangeWithPromptLocalized(
                            1,
                            question.answers().size(),
                            "TestService.prompt",
                            "TestService.invalid.value"
                             );
            isAnswerValid = question.answers().get(answerNumber - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
