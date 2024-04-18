package com.kukharev.health.check;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntitysTest {
    @Test
    public void testErrorEntityCreation() {
        ErrorEntity error = new ErrorEntity();
        assertNotNull(error);
    }

    @Test
    public void testServerEntityCreation() {
        ServerEntity server = new ServerEntity();
        assertNotNull(server);
    }
}
