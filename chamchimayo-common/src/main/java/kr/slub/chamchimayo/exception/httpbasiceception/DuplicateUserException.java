package kr.slub.chamchimayo.exception.httpbasiceception;

public class DuplicateUserException extends CustomException {
    public DuplicateUserException(Integer code, String message) {
        super(code, message);
    }
}
