package com.workshop.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

// DTO para un solo error de campo
@Data
@AllArgsConstructor
public class ViolationDto {

        private String field;
        private String message;

}
