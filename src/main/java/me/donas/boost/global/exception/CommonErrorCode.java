package me.donas.boost.global.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "입력 형식을 확인해주세요."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
	PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "이미지의 크기는 10MB를 초과할 수 없습니다."),
	FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "업로드에 실패했습니다."),
	INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "잘못된 파일 형식입니다."),
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
