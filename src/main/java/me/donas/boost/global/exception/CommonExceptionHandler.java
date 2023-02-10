package me.donas.boost.global.exception;

import static me.donas.boost.global.exception.CommonErrorCode.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import me.donas.boost.global.dto.ErrorResponse;
import me.donas.boost.global.dto.ErrorResponse.ValidationError;

@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(CommonException.class)
	public ResponseEntity<ErrorResponse> handleCommonException(CommonException e) {
		ErrorCode errorCode = e.errorCode();
		return ResponseEntity.status(errorCode.httpStatus()).body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(makeErrorResponse(e));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(INTERNAL_SERVER_ERROR));
	}

	private ErrorResponse makeErrorResponse(MethodArgumentNotValidException e) {
		List<ValidationError> validationErrorList = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(ValidationError::of)
			.toList();

		return ErrorResponse.of(INVALID_PARAMETER, validationErrorList);
	}
}
