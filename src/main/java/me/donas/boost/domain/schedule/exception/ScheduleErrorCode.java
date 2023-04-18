package me.donas.boost.domain.schedule.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import me.donas.boost.global.exception.ErrorCode;

@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {
	NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "해당 스케쥴 정보를 찾을 수 없습니다.");

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

