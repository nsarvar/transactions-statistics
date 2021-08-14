package com.n26.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.exception.BadRequestException;
import com.n26.exception.TimestampInFutureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(TimestampInFutureException.class)
    public void handleException(TimestampInFutureException e) {
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidFormatException.class)
    public void handleException(InvalidFormatException e) {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public void handleException(BadRequestException e) {
    }
}