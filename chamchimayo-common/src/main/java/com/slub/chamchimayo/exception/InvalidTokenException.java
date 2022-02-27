package com.slub.chamchimayo.exception;

import com.slub.chamchimayo.exception.httpbasiceception.CustomException;

public class InvalidTokenException extends CustomException {

    public InvalidTokenException(Integer code, String message) {
        super(code, message);
    }
}
