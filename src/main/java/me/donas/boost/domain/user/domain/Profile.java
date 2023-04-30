package me.donas.boost.domain.user.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Profile {
	String nickname;

	@Lob
	String introduce;

	String image;

	@Builder
	public Profile(String nickname, String introduce, String image) {
		this.nickname = nickname;
		this.introduce = introduce;
		this.image = image;
	}
}
