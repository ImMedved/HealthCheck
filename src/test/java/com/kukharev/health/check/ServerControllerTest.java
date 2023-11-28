package com.kukharev.health.check;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ServerController.class)
@ContextConfiguration(classes = {AppConfig.class})
// FIXME
class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServerService serverService;

    @Test
    void testGetServers() throws Exception {
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

        // Задаем поведение для вызова serverService.getAllServers()
        Mockito.when(serverService.getAllServers()).thenReturn(mockServers);

        // Запрос к эндпоинту и проверка ожидаемого результата
        mockMvc.perform(get("/servers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Server 1\",\"address\":\"http://server1.com\",\"errorCounter\":0}," +
                        "{\"id\":2,\"name\":\"Server 2\",\"address\":\"http://server2.com\",\"errorCounter\":0}]"));
    }

}

