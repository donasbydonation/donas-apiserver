package me.donas.boost.domain.preregistration.exception;

import lombok.RequiredArgsConstructor;
import me.donas.boost.global.exception.CommonException;
import me.donas.boost.global.exception.ErrorCode;

@RequiredArgsConstructor
public class DuplicateEmailException extends CommonException {
	private final ErrorCode errorCode;

	@Override
	public ErrorCode errorCode() {
		return this.errorCode;
	}
}
