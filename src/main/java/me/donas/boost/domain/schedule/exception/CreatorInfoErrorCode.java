package me.donas.boost.domain.schedule.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import me.donas.boost.global.exception.ErrorCode;

@RequiredArgsConstructor
public enum CreatorInfoErrorCode implements ErrorCode {
	DUPLICATE_CREATOR_NAME(HttpStatus.CONFLICT, "닉네임이 중복되었습니다."),
	NOT_FOUND_CREATOR_INFO(HttpStatus.NOT_FOUND, "해당 크리에이터 정보를 찾을 수 없습니다.");

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
