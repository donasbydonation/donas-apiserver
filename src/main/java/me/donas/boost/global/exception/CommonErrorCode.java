package me.donas.boost.global.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "입력 형식을 확인해주세요."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이메일이 중복되었습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public HttpStatus httpStatus() {
		return this.httpStatus;
	}

	@Override
	public String message() {
		return this.message;
	}
}
