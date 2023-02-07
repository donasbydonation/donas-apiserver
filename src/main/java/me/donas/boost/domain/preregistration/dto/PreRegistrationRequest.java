package me.donas.boost.domain.preregistration.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import me.donas.boost.domain.preregistration.domain.PreRegistration;

public record PreRegistrationDto(
	@NotEmpty(message = "이메일 형식을 확인해주세요.")
	@Email(message = "이메일 형식을 확인해주세요.")
	String email
) {
	public PreRegistration toEntity() {
		return PreRegistration.builder().email(email).build();
	}
}
