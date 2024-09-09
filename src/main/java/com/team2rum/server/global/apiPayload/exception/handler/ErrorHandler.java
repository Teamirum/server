package com.team2rum.server.global.apiPayload.exception.handler;


import com.team2rum.server.global.apiPayload.code.BaseErrorCode;
import com.team2rum.server.global.apiPayload.exception.GeneralException;

public class ErrorHandler extends GeneralException {

    public ErrorHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
