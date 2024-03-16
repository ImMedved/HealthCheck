package com.kukharev.health.check;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppConfigTest {
    @Test
    public void testRestTemplateBeanCreation() {
        AppConfig appConfig = new AppConfig();
        assertNotNull(appConfig.restTemplate());
    }
}

