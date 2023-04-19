package me.donas.boost.domain.user.dto;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import me.donas.boost.domain.user.domain.Gender;
import me.donas.boost.domain.user.domain.Profile;
import me.donas.boost.domain.user.domain.Role;
import me.donas.boost.domain.user.domain.User;

public record SignupRequest(@NotBlank String username, @NotBlank String password, @NotBlank @Email String email,
							@NotBlank String nickname, Gender gender, LocalDate birthDay, boolean agreePromotion) {

	public User toEntity(PasswordEncoder encoder) {
		String password = encoder.encode(this.password);
		Gender gender = getGender();
		Profile profile = Profile.builder().nickname(this.nickname).build();

		return User.builder()
			.username(username)
			.password(password)
			.email(email)
			.gender(gender)
			.birthDay(birthDay)
			.agreePromotion(agreePromotion)
			.profile(profile)
			.role(Role.ROLE_USER).build();
	}

	private Gender getGender() {
		return this.gender == null ? Gender.NONE : this.gender;
	}
}
