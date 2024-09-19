package org.scoula.global.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.scoula.global.apiPayload.code.BaseErrorCode;
import org.scoula.global.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
