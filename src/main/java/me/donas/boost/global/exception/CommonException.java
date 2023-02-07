package me.donas.boost.global.exception;

public abstract class CommonException extends RuntimeException {
	public abstract ErrorCode errorCode();
}
