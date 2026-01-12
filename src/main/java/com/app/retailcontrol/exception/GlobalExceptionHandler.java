package com.app.retailcontrol.exception;

import com.app.retailcontrol.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleJsonParseException(HttpMessageNotReadableException exception){
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                exception.getMessage(),
                "error",
                400,
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFound(ResourceNotFoundException exception) {
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                exception.getMessage(),
                "error",
                404,
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponseDTO);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceAlreadyExists(ResourceAlreadyExistsException exception) {
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                exception.getMessage(),
                "error",
                409,
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleAnyException(Exception exception) {
        ApiResponseDTO<Object> apiResponseDTO = new ApiResponseDTO<>(
                "Unexpected error occurred",
                "error",
                500,
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseDTO);
    }

}
