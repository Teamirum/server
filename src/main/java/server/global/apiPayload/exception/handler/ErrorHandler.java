package server.global.apiPayload.exception.handler;


import server.global.apiPayload.code.BaseErrorCode;
import server.global.apiPayload.exception.GeneralException;

public class ErrorHandler extends GeneralException {

    public ErrorHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
