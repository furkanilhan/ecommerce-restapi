package com.furkan.ecommerce.exception;

import com.furkan.ecommerce.payload.response.ErrorResponse;
import com.furkan.ecommerce.payload.response.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGlobalException(Exception ex) {
        log.error("Internal Server Error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal Server Error"));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<MessageResponse> handleNullPointerException(NullPointerException ex) {
        log.error("NullPointerException occurred: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Unexpected error occurred."));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Entity Not Found", ex.getMessage());
        log.error("Entity Not Found: ", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<MessageResponse> handleCustomException(CustomException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(new MessageResponse(ex.getMessage()));
    }
}