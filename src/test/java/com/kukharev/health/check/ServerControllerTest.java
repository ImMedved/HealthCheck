package com.kukharev.health.check;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;

@WebMvcTest(ServerController.class)
public class ServerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServerService serverService;

    @Test
    public void testGetServers() throws Exception {
        ServerEntity server1 = new ServerEntity();
        server1.setId(1L);
        ServerEntity server2 = new ServerEntity();
        server2.setId(2L);

        List<ServerEntity> serverList = Arrays.asList(server1, server2);

        when(serverService.getAllServers()).thenReturn(serverList);

        mockMvc.perform(get("/servers"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$[0].id", is(1)))
                .andExpect((ResultMatcher) jsonPath("$[1].id", is(2)));
    }
}

