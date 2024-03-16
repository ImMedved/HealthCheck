package com.kukharev.health.check;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ErrorLoggingServiceTest {
    @Mock
    private ErrorRepository errorRepository;

    @InjectMocks
    private ErrorLoggingService errorLoggingService;

    @Test
    public void testLogError() {
        ServerEntity server = new ServerEntity();
        ErrorEntity error = new ErrorEntity();
        error.setServer(server);

        errorLoggingService.logError(server);

        verify(errorRepository, times(1)).save(error);
    }
}

