package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Arrays;import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private TestServiceImpl testService;

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    private Student student;

    private Question question;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
        student = new Student("Name", "Surname");
        question = new Question("Enter 2",
                Arrays.asList(new Answer("1", false), new Answer("2", true)));
    }

    @DisplayName("Verify print services and test resaults")
    @Test
    void executeTestForTest() {

        when(questionDao.findAll()).thenReturn(List.of(question));
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(2);

        TestResult result = testService.executeTestFor(student);

        assertNotNull(result);
        assertEquals(1, result.getRightAnswersCount());
        verify(ioService, atLeastOnce()).printLine(anyString());
        verify(ioService, atLeastOnce()).printFormattedLine(anyString());
        verify(ioService, times(1)).readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString());
    }
}
