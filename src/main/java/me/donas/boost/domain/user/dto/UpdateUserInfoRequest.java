package me.donas.boost.domain.user.dto;

import java.time.LocalDate;

import me.donas.boost.domain.user.domain.Gender;

public record UpdateUserInfoRequest(Gender gender, LocalDate birthDay, boolean agreePromotion) {

	@Override
	public Gender gender() {
		return this.gender == null ? Gender.NONE : this.gender;
	}
}
