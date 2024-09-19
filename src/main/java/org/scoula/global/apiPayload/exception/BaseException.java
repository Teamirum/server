package org.scoula.global.apiPayload.exception;

public abstract class BaseException extends RuntimeException{
	public abstract BaseExceptionType getExceptionType();
}
