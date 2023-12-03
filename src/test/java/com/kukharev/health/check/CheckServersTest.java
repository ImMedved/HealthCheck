package com.kukharev.health.check;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class CheckServersTest {

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private ErrorNotificationService errorNotificationService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ServerService serverService;

    @Test
    void testCheckServers() {
        ServerEntity serverEntity1 = new ServerEntity();
        ServerEntity serverEntity2 = new ServerEntity();
        List<ServerEntity> servers = Arrays.asList(serverEntity1, serverEntity2);

        serverEntity1.setAddress("https://example1.com");
        serverEntity2.setAddress("https://example2.com");

        when(serverRepository.findAll()).thenReturn(servers);

        ResponseEntity<String> successResponse = new ResponseEntity<>("Success", HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(successResponse);

        serverService.checkServers();

        verify(serverRepository, times(1)).findAll();
        verify(restTemplate, times(2)).getForEntity(anyString(), eq(String.class));
        verify(errorNotificationService, never()).notifyError(any());
    }
}
