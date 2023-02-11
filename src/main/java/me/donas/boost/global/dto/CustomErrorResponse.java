package me.donas.boost.global.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

import me.donas.boost.global.exception.ErrorCode;

public record CustomErrorResponse(
	String name,
	String httpStatus,
	String message,
	@JsonInclude(JsonInclude.Include.NON_EMPTY) List<ValidationError> errors
) {

	public record ValidationError(String field, String message) {
		public static ValidationError of(FieldError fieldError) {
			return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
		}
	}

	public static CustomErrorResponse of(ErrorCode errorCode) {
		return new CustomErrorResponse(errorCode.name(), errorCode.httpStatus().name(), errorCode.message(),
			new ArrayList<>());
	}

	public static CustomErrorResponse of(ErrorCode errorCode, List<ValidationError> errors) {
		return new CustomErrorResponse(errorCode.name(), errorCode.httpStatus().name(), errorCode.message(), errors);
	}
}
