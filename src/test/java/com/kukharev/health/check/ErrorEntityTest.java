package com.kukharev.health.check;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorEntityTest {
    @Test
    public void testErrorEntityCreation() {
        ErrorEntity error = new ErrorEntity();
        assertNotNull(error);
    }
}
