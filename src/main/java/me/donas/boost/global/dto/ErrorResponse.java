package me.donas.boost.global.dto;

public record ErrorResponse(String message) {
	public static  ErrorResponse of(String message) {
		return new ErrorResponse(message);
	}
}
