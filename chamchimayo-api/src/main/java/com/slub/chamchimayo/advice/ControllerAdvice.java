package com.slub.chamchimayo.advice;

import com.slub.chamchimayo.exception.dto.ExceptionResponse;
import com.slub.chamchimayo.exception.httpbasiceception.BadRequestException;
import com.slub.chamchimayo.exception.httpbasiceception.DuplicateUserException;
import com.slub.chamchimayo.exception.httpbasiceception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundException(NotFoundException e) {
        return new ExceptionResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequestException(BadRequestException e) {
        return new ExceptionResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleDuplicateException(DuplicateUserException e) {
        return new ExceptionResponse(e.getCode(), e.getMessage());
    }
}
