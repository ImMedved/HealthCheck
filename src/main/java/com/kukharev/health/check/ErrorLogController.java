package com.kukharev.health.check;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class ErrorLogController {
    @GetMapping("/error_log")
    public String showErrorLogPage() {
        return "error_log";
    }
}

