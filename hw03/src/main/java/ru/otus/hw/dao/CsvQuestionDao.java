package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (Objects.isNull(is)) {
            throw new QuestionReadException("CSV file not found: " + fileName);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<QuestionDto> list = new CsvToBeanBuilder<QuestionDto>(reader)
                .withType(QuestionDto.class)
                .withSeparator(';')
                .withSkipLines(1)
                .build()
                .parse();
        try {
            is.close();
        } catch (IOException e) {
            throw new QuestionReadException("InputStream is null");
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new QuestionReadException("BufferedReader is null");
        }
        return list.stream().map(QuestionDto::toDomainObject).toList();
    }
}
