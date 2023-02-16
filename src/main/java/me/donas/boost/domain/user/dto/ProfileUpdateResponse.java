package me.donas.boost.domain.user.dto;

import me.donas.boost.domain.user.domain.Profile;

public record ProfileUpdateResponse(String nickname, String introduce, String image) {
	public static ProfileUpdateResponse of(Profile profile) {
		return new ProfileUpdateResponse(profile.getNickname(), profile.getIntroduce(), profile.getImage());
	}
}
