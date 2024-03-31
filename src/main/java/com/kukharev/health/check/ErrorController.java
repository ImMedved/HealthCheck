package com.kukharev.health.check;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/errors")
public class ErrorController {

    private final ErrorRepository errorRepository;

    public ErrorController(ErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }

    @GetMapping
    public ResponseEntity<List<ErrorDTO>> getAllErrors() {
        List<ErrorEntity> errors = errorRepository.findAll();
        List<ErrorDTO> errorDTOs = convertToErrorDTOs(errors);
        return ResponseEntity.ok(errorDTOs);
    }

    private List<ErrorDTO> convertToErrorDTOs(List<ErrorEntity> errors) {
        return errors.stream()
                .map(this::convertToErrorDTO)
                .collect(Collectors.toList());
    }

    private ErrorDTO convertToErrorDTO(ErrorEntity errorEntity) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setId(errorEntity.getId());
        errorDTO.setTimestamp(errorEntity.getTimestamp());
        errorDTO.setServerName(errorEntity.getServer().getName());
        return errorDTO;
    }
}
