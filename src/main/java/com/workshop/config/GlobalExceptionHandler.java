package com.workshop.config;

import com.workshop.exception.NotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

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
