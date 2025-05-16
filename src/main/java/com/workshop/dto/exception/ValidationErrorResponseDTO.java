package com.workshop.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

// DTO para la respuesta de errores
@Data
public class ValidationErrorResponseDTO {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private List<ViolationDto> violations;

    public ValidationErrorResponseDTO(HttpStatus status, List<ViolationDto> violations) {
        this.status     = status.value();
        this.error      = status.getReasonPhrase();
        this.violations = violations;
    }
}
