package com.kukharev.health.check;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ErrorLogController.class)
public class ErrorLogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShowErrorLogPage() throws Exception {
        mockMvc.perform(get("/error_log"))
                .andExpect(status().isOk())
                .andExpect(view().name("error_log"));
    }
}
