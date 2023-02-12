package me.donas.boost.domain.user.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String username;

	String password;

	String email;

	@Enumerated(EnumType.STRING)
	Gender gender;

	@Enumerated(EnumType.STRING)
	Role role;

	LocalDate birthDay;

	@Embedded
	Account account;

	String phoneNumber;

	boolean agreePromotion;

	LocalDateTime lastChangePasswordDate = LocalDateTime.now();

	LocalDateTime lastLoginDate;

	boolean isWithdraw;

	LocalDateTime withdrawDate;

	@Embedded
	Profile profile;

	@Embedded
	RefreshToken refreshToken;

	@Builder
	public User(String username, String password, String email, Gender gender, LocalDate birthDay,
		boolean agreePromotion, Profile profile) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.gender = gender;
		this.birthDay = birthDay;
		this.agreePromotion = agreePromotion;
		this.profile = profile;
		this.role = Role.ROLE_USER;
	}

	public void withdraw() {
		this.isWithdraw = true;
		this.withdrawDate = LocalDateTime.now();
	}

	public String login() {
		this.lastLoginDate = LocalDateTime.now();
		return updateRefreshToken();
	}

	public String updateRefreshToken() {
		String token = UUID.randomUUID().toString();
		this.refreshToken = RefreshToken.builder().token(token).build();
		return token;
	}

	public void logout() {
		this.refreshToken.initialize();
	}

	public void updatePassword(String updatePassword) {
		this.password = updatePassword;
		this.lastChangePasswordDate = LocalDateTime.now();
	}
}
