package com.slub.chamchimayo.exception.httpbasiceception;

public class BadRequestException extends CustomException{

    public BadRequestException(Integer code, String message) {
        super(code, message);
    }
}
