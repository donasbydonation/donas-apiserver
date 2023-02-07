package me.donas.boost.domain.preregistration.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import me.donas.boost.domain.preregistration.domain.PreRegistration;

public record PreRegistrationRequest(
	@NotEmpty
	@Email
	String email
) {
	public PreRegistration toEntity() {
		return PreRegistration.builder().email(email).build();
	}
}
