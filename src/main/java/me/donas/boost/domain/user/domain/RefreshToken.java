package me.donas.boost.domain.user.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

	@Column
	LocalDateTime refreshTokenValidDate;

	@Column
	String token;

	@Builder
	public RefreshToken(String token) {
		this.refreshTokenValidDate = LocalDateTime.now().plusMonths(1);
		this.token = token;
	}

	public boolean isValid(String token) {
		return this.token.equals(token) && this.refreshTokenValidDate.isAfter(LocalDateTime.now());
	}

	public void initialize() {
		this.token = "";
		this.refreshTokenValidDate = LocalDateTime.now();
	}
}
