package com.slub.chamchimayo.exception;

import com.slub.chamchimayo.exception.httpbasiceception.BadRequestException;
import com.slub.chamchimayo.exception.httpbasiceception.CustomException;
import com.slub.chamchimayo.exception.httpbasiceception.DuplicateUserException;
import com.slub.chamchimayo.exception.httpbasiceception.NotFoundException;
import lombok.Getter;

@Getter
public enum ExceptionWithCodeAndMessage {
    // 서버 관련 : 5XX
    INVALID_ACCESS_TOKEN(new InvalidTokenException(500, "Invalid access token.")),
    INVALID_REFRESH_TOKEN(new InvalidTokenException(500, "INVALID_REFRESH_TOKEN")),
    NOT_EXPIRED_TOKEN_YET(new InvalidTokenException(500, "Not expired token yet.")),

    // 유저 관련 : 6XX
    NOT_FOUND_USER(new NotFoundException(600, "해당하는 유저가 없습니다.")),
    DUPLICATE_USER(new DuplicateUserException(601, "이미 존재하는 회원입니다")),

    // 로그인 관련 : 7XX

    // 제약 관련 : 8XX
    INVALID_INPUT_LENGTH(new BadRequestException(800, "입력값의 길이가 적절하지 않습니다."));

    private final CustomException exception;

    ExceptionWithCodeAndMessage(CustomException e) {
        this.exception = e;
    }

    public String findMessage() {
        return exception.getMessage();
    }

    public Integer findCode() {
        return exception.getCode();
    }

}
