package me.donas.boost.domain.emailcertificate.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import me.donas.boost.global.exception.ErrorCode;

@RequiredArgsConstructor
public enum EmailCertificateErrorCode implements ErrorCode {
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일 입니다."),
	CERTIFY_TIMEOUT(HttpStatus.BAD_REQUEST, "이메일 인증 코드가 만료되었습니다."),
	ALREADY_CERTIFIED(HttpStatus.BAD_REQUEST, "이미 인증되었습니다."),
	INCORRECT_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다."),
	REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 인증 요청을 확인할 수 없습니다.");

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
