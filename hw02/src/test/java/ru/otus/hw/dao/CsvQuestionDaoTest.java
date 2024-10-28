package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



class CsvQuestionDaoTest {

    private CsvQuestionDao csvQuestionDao;

    private TestFileNameProvider testFileNameProvider;

    @BeforeEach
    void setUp() {
        testFileNameProvider = mock(TestFileNameProvider.class);
        csvQuestionDao = new CsvQuestionDao(testFileNameProvider);
    }

    @DisplayName("Should find all question from csv file")
    @Test
    void testFindAllReturnsQuestionsFromCsv() {
        when(testFileNameProvider.getTestFileName()).thenReturn("questionsTest.csv");
        List<Question> questions = csvQuestionDao.findAll();
        Assertions.assertFalse(questions.isEmpty(), "The list of questions should not be empty.");
    }


    @Test
    public void testFindAllFileNotFound() {
        when(testFileNameProvider.getTestFileName()).thenReturn("non-existent-file.csv");

        assertThatThrownBy(() -> csvQuestionDao.findAll())
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("CSV file not found: non-existent-file.csv");
    }
}

