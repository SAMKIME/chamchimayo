package kr.slub.chamchimayo.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private Integer code;

    private String message;
}
