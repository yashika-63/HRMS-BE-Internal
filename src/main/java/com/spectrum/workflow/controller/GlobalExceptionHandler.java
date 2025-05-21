package com.spectrum.workflow.controller;

import com.spectrum.model.ExceptionLog;
import com.spectrum.repository.ExceptionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // Create an ExceptionLog object
        ExceptionLog exceptionLog = new ExceptionLog(
                ex.getClass().getName(),
                ex.getMessage(),
                LocalDateTime.now(),
                getStackTraceAsString(ex)
        );

        // Save exception details to the database
        exceptionLogRepository.save(exceptionLog);

        // Return a generic error response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("An error occurred. Please try again later.");
    }

    private String getStackTraceAsString(Exception ex) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
