package me.donas.boost.domain.user.dto;

import java.time.LocalDate;

import me.donas.boost.domain.user.domain.Gender;
import me.donas.boost.domain.user.domain.User;

public record UserInfoResponse(Gender gender, LocalDate birthDay, boolean agreePromotion) {
	public static UserInfoResponse of(User user) {
		return new UserInfoResponse(user.getGender(), user.getBirthDay(), user.isAgreePromotion());
	}
}
