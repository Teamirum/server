package org.scoula.global.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.scoula.global.apiPayload.code.BaseCode;
import org.scoula.global.apiPayload.code.ReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    _OK(HttpStatus.OK, "COMMON200", "성공입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
            .isSuccess(true)
            .code(code)
            .message(message)
            .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
            .httpStatus(httpStatus)
            .isSuccess(true)
            .code(code)
            .message(message)
            .build();
    }
}
