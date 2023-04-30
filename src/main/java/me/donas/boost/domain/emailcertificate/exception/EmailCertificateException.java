package me.donas.boost.domain.emailcertificate.exception;

import lombok.RequiredArgsConstructor;
import me.donas.boost.global.exception.CommonException;
import me.donas.boost.global.exception.ErrorCode;

@RequiredArgsConstructor
public class EmailCertificateException extends CommonException {
	private final ErrorCode errorCode;

	@Override
	public ErrorCode errorCode() {
		return errorCode;
	}
}
