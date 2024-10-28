package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TestServiceImplTest {

    private TestServiceImpl testService;
    private IOService ioService;
    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    public void setUp() {
        ioService = Mockito.mock(IOService.class);
        fileNameProvider = Mockito.mock(TestFileNameProvider.class);
        QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    public void testExecuteTestFor() {
        when(fileNameProvider.getTestFileName()).thenReturn("questionsTest.csv");
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(1, 1, 3, 1, 4);

        Student student = new Student("Name","Surname");

        TestResult result = testService.executeTestFor(student);

        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getRightAnswersCount()).isEqualTo(5);
    }
}
