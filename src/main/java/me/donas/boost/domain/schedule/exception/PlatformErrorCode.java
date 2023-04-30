package me.donas.boost.domain.schedule.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import me.donas.boost.global.exception.ErrorCode;

@RequiredArgsConstructor
public enum PlatformErrorCode implements ErrorCode {
	NOT_FOUND_PLATFORM(HttpStatus.NOT_FOUND, "해당 크리에이터의 플랫폼 정보를 찾을 수 없습니다."),
	DUPLICATE_PLATFORM(HttpStatus.CONFLICT, "크리에이터의 플랫폼 정보가 중복되었습니다.");

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

