package com.kukharev.health.check;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

// Добавляем в ServerService метод для отправки запросов каждую минуту
@Service
public class ServerService {
    @Scheduled(fixedDelay = 60000)
    public void sendRequestsToServers() {
        checkServers();
    }

    public void checkServers() {
        // Реализация метода для проверки серверов будет добавлена здесь
    }

    public List<ServerEntity> getAllServers() {
        // Вернуть все серверы из репозитория
        return null; // Реализация будет добавлена позже
    }
}



