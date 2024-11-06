package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;


@SpringBootTest
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @MockBean
    private TestFileNameProvider testFileNameProvider;

    @MockBean
    private LocaleConfig localeConfig;

    @MockBean
    private TestConfig testConfig;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("Should find all question from csv file")
    @Test
    void testFindAllReturnsQuestionsFromCsv() {
        List<Question> questions = csvQuestionDao.findAll();
        Assertions.assertFalse(questions.isEmpty(), "The list of questions should not be empty.");
    }


    @DisplayName("Should return file not found exception")
    @Test
    public void testFindAllFileNotFound() {
        when(testFileNameProvider.getTestFileName()).thenReturn("non-existent-file.csv");
        when(localeConfig.getLocale()).thenReturn(Locale.getDefault());
        when(testConfig.getRightAnswersCountToPass()).thenReturn(5);
        assertThatThrownBy(() -> csvQuestionDao.findAll())
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("CSV file not found: non-existent-file.csv");
    }
}

