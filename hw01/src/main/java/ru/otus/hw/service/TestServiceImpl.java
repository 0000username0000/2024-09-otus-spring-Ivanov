package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.CsvQuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final CsvQuestionDao csvQuestionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        csvQuestionDao.findAll().forEach(question -> {
            ioService.printLine("-----------------------------------");
            ioService.printLine(question.text() + "\n");
            question.answers().forEach(answer -> ioService.printLine(answer.text()));
        });
        ioService.printLine("-----------------------------------");
    }
}
