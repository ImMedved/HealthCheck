package com.kukharev.health.check;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ErrorLoggingService {
    private final ErrorRepository errorRepository;

    public ErrorLoggingService(ErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }

    public void logError(ServerEntity server) {
        ErrorEntity error = new ErrorEntity();
        error.setServer(server);
        error.setTimestamp(LocalDateTime.now());
        errorRepository.save(error);
    }
}

