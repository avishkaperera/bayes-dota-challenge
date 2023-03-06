package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    private ResponseEntity<ErrorResponse> handleIllegalArgExceptions(IllegalArgumentException ex) {
        log.error("Handling illegal argument exception [{}]", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("80200", ex.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse, httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    private ResponseEntity<ErrorResponse> handleRuntimeExceptions(RuntimeException ex) {
        log.error("Handling runtime exception [{}]", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("80000", ex.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
