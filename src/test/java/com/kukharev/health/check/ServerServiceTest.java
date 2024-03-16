package com.kukharev.health.check;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ServerServiceTest {
    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private ServerService serverService;

    @Test
    public void testGetAllServers() {
        ServerEntity server1 = new ServerEntity();
        ServerEntity server2 = new ServerEntity();
        List<ServerEntity> serverList = Arrays.asList(server1, server2);

        when(serverRepository.findAll()).thenReturn(serverList);

        List<ServerEntity> result = serverService.getAllServers();

        assertEquals(2, result.size());
    }
}

