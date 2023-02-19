package me.donas.boost.domain.user.dto;

import me.donas.boost.domain.user.domain.Profile;

public record ProfileResponse(String nickname, String introduce, String image) {
	public static ProfileResponse of(Profile profile) {
		return new ProfileResponse(profile.getNickname(), profile.getIntroduce(), profile.getImage());
	}
}
