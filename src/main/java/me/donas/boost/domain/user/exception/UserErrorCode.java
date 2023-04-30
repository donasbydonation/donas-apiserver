package me.donas.boost.domain.user.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import me.donas.boost.global.exception.ErrorCode;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "닉네임이 중복되었습니다."),
	DUPLICATE_USERNAME(HttpStatus.CONFLICT, "아이디가 중복되었습니다."),
	INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
	INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰의 시그니처가 잘못되었습니다."),
	UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰 입니다."),
	EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
	EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레쉬 토큰이 만료되었습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호 입니다."),
	NOTFOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다.");

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
