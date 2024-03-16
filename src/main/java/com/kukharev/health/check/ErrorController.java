package com.kukharev.health.check;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/errors")
public class ErrorController {
    private final ErrorRepository errorRepository;

    public ErrorController(ErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }

    @GetMapping
    public ResponseEntity<List<ErrorEntity>> getAllErrors() {
        List<ErrorEntity> errors = errorRepository.findAll();
        return ResponseEntity.ok(errors);
    }
}


