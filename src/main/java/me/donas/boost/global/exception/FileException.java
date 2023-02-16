package me.donas.boost.global.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileException extends CommonException {
	private final ErrorCode errorCode;

	@Override
	public ErrorCode errorCode() {
		return this.errorCode;
	}
}
