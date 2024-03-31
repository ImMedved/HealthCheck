package com.kukharev.health.check;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = AppConfig.class)
@ContextConfiguration
public class ServerServiceTest {

    @Autowired
    private ServerService serverService;

    @MockBean
    private ServerRepository serverRepository;

    @MockBean
    private ErrorRepository errorRepository;

    @Test
    public void testLogError() {
        // Подготовка данных
        String serverUrl = "http://example.com";
        ServerEntity serverEntity = new ServerEntity();
        serverEntity.setId(1L);
        serverEntity.setAddress(serverUrl);
        serverEntity.setErrorCounter(0);

        // Указываем, как должен вести себя serverRepository при вызове метода findByUrl
        when(serverRepository.findByUrl(serverUrl)).thenReturn(serverEntity);

        // Выполнение тестируемого метода
        serverService.logError(serverUrl);

        // Проверка, что методы репозиториев были вызваны с ожидаемыми параметрами
        verify(serverRepository, times(1)).findByUrl(serverUrl);
        verify(errorRepository, times(1)).save(any(ErrorEntity.class));

        // Проверка, что errorCounter сервера увеличился на 1
        assert serverEntity.getErrorCounter() == 1;

        // Проверка, что timestamp ошибки установлен корректно
        verify(errorRepository, times(1)).save(argThat(error -> error.getTimestamp() != null && error.getTimestamp().isBefore(LocalDateTime.now())));
    }
}
