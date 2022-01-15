package com.slub.chamchimayo.exception.httpbasiceception;

import com.slub.chamchimayo.exception.httpbasiceception.CustomException;

public class DuplicateUserException extends CustomException {
    public DuplicateUserException(Integer code, String message) {
        super(code, message);
    }
}
