package me.donas.boost.global.exception;

import static me.donas.boost.global.exception.CommonErrorCode.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import me.donas.boost.global.dto.CustomErrorResponse;
import me.donas.boost.global.dto.CustomErrorResponse.ValidationError;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(CommonException.class)
	public ResponseEntity<CustomErrorResponse> handleCommonException(CommonException e) {
		ErrorCode errorCode = e.errorCode();
		return ResponseEntity.status(errorCode.httpStatus()).body(CustomErrorResponse.of(errorCode));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(makeErrorResponse(e));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<CustomErrorResponse> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CustomErrorResponse.of(UNAUTHORIZED));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorResponse> handleException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomErrorResponse.of(INTERNAL_SERVER_ERROR));
	}

	private CustomErrorResponse makeErrorResponse(MethodArgumentNotValidException e) {
		List<ValidationError> validationErrorList = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(ValidationError::of)
			.toList();

		return CustomErrorResponse.of(INVALID_PARAMETER, validationErrorList);
	}
}
