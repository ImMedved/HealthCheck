package Test;

import ServerCheck.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {AppConfig.class})
public class ServerServiceTest {

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private ErrorNotificationService errorNotificationService;

    @InjectMocks
    private ServerService serverService;

    @Test
    public void testGetAllServers() {
        // Фейковые данные
        ServerEntity server1 = new ServerEntity();
        server1.setId(1L);
        server1.setName("Server 1");
        server1.setAddress("http://server1.com");
        server1.setErrorCounter(0);

        ServerEntity server2 = new ServerEntity();
        server2.setId(2L);
        server2.setName("Server 2");
        server2.setAddress("http://server2.com");
        server2.setErrorCounter(0);

        List<ServerEntity> mockServers = Arrays.asList(server1, server2);

        // Задаем поведение  serverRepository.findAll()
        Mockito.when(serverRepository.findAll()).thenReturn(mockServers);

        // Вызываем метод
        List<ServerEntity> result = serverService.getAllServers();

        // Проверяем, что результат соответствует ожиданиям
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Server 1", result.get(0).getName());
        Assertions.assertEquals("http://server2.com", result.get(1).getAddress());
    }

}

