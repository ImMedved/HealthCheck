package com.kukharev.health.check;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AppConfig.class, ServerService.class})
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
        String serverUrl = "http://example.com";
        ServerEntity serverEntity = new ServerEntity();
        serverEntity.setId(1L);
        serverEntity.setAddress(serverUrl);
        serverEntity.setErrorCounter(0);

        List<ServerEntity> serverEntities = Collections.singletonList(serverEntity);

        when(serverRepository.findAll()).thenReturn(serverEntities);

        serverService.invokeServerService();

        verify(serverRepository, times(1)).findAll();
    }
}
