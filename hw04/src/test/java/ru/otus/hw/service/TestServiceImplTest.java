package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TestServiceImplTest {

    @Autowired
    private TestServiceImpl testService;

    @Mock
    private QuestionDao questionDao;

    @MockBean
    private LocalizedIOService ioService;


    @BeforeEach
    public void setUp() {
        Question question1 = new Question("Question 1",
                List.of(new Answer("Answer 1", true),
                        new Answer("Answer 2", false)));
        Question question2 = new Question("Question 2",
                List.of(new Answer("Answer 1", false),
                        new Answer("Answer 2", true)));
        Question question3 = new Question("Question 3",
                List.of(new Answer("Answer 1", false),
                        new Answer("Answer 2", false),
                        new Answer("Answer 3", true)));
        when(questionDao.findAll()).thenReturn(Arrays.asList(question1, question2, question3));
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(1,2,3);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    public void testExecuteTestFor() {

        Student student = new Student("Name","Surname");

        TestResult result = testService.executeTestFor(student);

        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getRightAnswersCount()).isEqualTo(3);
    }
}
