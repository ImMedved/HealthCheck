package com.kukharev.health.check;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerEntityTest {
    @Test
    public void testServerEntityCreation() {
        ServerEntity server = new ServerEntity();
        assertNotNull(server);
    }
}

