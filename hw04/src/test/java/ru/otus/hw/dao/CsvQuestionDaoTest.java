package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.Question;

import java.util.List;


@SpringBootTest
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

//    @Autowired
//    private TestFileNameProvider testFileNameProvider;


    @BeforeEach
    void setUp() {

    }

    @DisplayName("Should find all question from csv file")
    @Test
    void testFindAllReturnsQuestionsFromCsv() {
        List<Question> questions = csvQuestionDao.findAll();
        Assertions.assertFalse(questions.isEmpty(), "The list of questions should not be empty.");
    }


//    @DisplayName("Should return file not found exception")
//    @Test
//    public void testFindAllFileNotFound() {
//        InputStream is = getClass().getClassLoader().getResourceAsStream("non-existent-file.csv");
//        when(testFileNameProvider.getTestFileName()).thenReturn("non-existent-file.csv");
//        assertThatThrownBy(() -> csvQuestionDao.findAll())
//                .isInstanceOf(QuestionReadException.class)
//                .hasMessageContaining("CSV file not found: non-existent-file.csv");
//    }
}

