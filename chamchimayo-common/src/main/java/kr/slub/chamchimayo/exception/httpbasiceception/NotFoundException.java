package kr.slub.chamchimayo.exception.httpbasiceception;

public class NotFoundException extends CustomException {

    public NotFoundException(Integer code, String message) {
        super(code, message);
    }
}
