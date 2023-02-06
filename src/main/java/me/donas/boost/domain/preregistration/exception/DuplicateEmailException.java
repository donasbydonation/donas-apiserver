package me.donas.boost.domain.preregistration.exception;

public class DuplicateEmailException extends RuntimeException {
	public DuplicateEmailException(String message) {
		super(message);
	}
}
