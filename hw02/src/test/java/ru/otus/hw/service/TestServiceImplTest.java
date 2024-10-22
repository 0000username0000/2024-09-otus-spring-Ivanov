package ru.otus.hw.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;

class TestServiceImplTest {

    @InjectMocks
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;
    //Нужно написать интеграционный тест класса, читающего вопросы и юнит-тест сервиса с моком зависимости.
    // Под интеграционностью тут понимается интеграция с файловой системой. В остальном, это должен быть юнит-тест
    void executeTestFor() {
    }
}
