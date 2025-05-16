package com.workshop.config;

import com.workshop.dto.exception.ValidationErrorResponseDTO;
import com.workshop.dto.exception.ViolationDto;
import com.workshop.exception.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Map<String,String> onConstraintViolation(ConstraintViolationException ex) {
//        return ex.getConstraintViolations().stream()
//                .collect(Collectors.toMap(
//                        v -> v.getPropertyPath().toString(),
//                        ConstraintViolation::getMessage
//                ));
//    }

    // Captura errores de @Valid / @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleRequestBodyValidation(
            MethodArgumentNotValidException ex) {

        List<ViolationDto> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ViolationDto(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        var body = new ValidationErrorResponseDTO(
                HttpStatus.BAD_REQUEST, violations
        );
        return ResponseEntity.badRequest().body(body);
    }

    // Captura errores de validator.validate(req, group)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleConstraintViolations(
            ConstraintViolationException ex) {

        List<ViolationDto> violations = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .distinct()
                .map(path -> {
                    String message = ex.getConstraintViolations().stream()
                            .filter(v -> v.getPropertyPath().toString().equals(path))
                            .map(ConstraintViolation::getMessage)
                            .findFirst().orElse("invalid");
                    return new ViolationDto(path, message);
                })
                .collect(Collectors.toList());

        var body = new ValidationErrorResponseDTO(
                HttpStatus.BAD_REQUEST, violations
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> onDuplicate(DuplicateKeyException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> onNotFound(NotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }
}
