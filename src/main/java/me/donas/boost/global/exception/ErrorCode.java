package me.donas.boost.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	String name();
	HttpStatus httpStatus();
	String message();
}
