package me.donas.boost.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import me.donas.boost.domain.user.domain.Profile;

public record ProfileUpdateRequest(@NotBlank String nickname, String introduce) {
	public Profile toEntity(String image) {
		return Profile.builder().image(image).nickname(nickname).introduce(introduce).build();
	}
}
