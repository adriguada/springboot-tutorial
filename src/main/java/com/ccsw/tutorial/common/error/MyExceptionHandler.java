package com.ccsw.tutorial.common.error;

import com.ccsw.tutorial.common.error.exceptions.DeleteResourceException;
import com.ccsw.tutorial.common.error.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(DeleteResourceException.class)
    public ResponseEntity<ErrorDto> handleResourceDeletionError(DeleteResourceException ex, HttpServletRequest request) {

        ErrorDto error = new ErrorDto(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", ex.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDto> handleResourceValidationError(ValidationException ex, HttpServletRequest request) {

        ErrorDto error = new ErrorDto(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", ex.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
