package me.donas.boost.domain.preregistration.dto;

import me.donas.boost.domain.preregistration.domain.PreRegistration;

public record PreRegistrationDto(String email) {
	public PreRegistration toEntity() {
		return PreRegistration.builder().email(email).build();
	}
}
