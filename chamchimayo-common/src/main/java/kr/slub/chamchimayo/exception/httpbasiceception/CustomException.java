package kr.slub.chamchimayo.exception.httpbasiceception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final Integer code;

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}