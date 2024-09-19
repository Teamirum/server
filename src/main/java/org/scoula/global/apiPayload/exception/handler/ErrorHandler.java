package org.scoula.global.apiPayload.exception.handler;


import org.scoula.global.apiPayload.code.BaseErrorCode;
import org.scoula.global.apiPayload.exception.GeneralException;

public class ErrorHandler extends GeneralException {

    public ErrorHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
