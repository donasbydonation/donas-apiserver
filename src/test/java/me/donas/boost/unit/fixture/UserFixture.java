package me.donas.boost.unit.fixture;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.test.util.ReflectionTestUtils;

import me.donas.boost.domain.user.domain.Gender;
import me.donas.boost.domain.user.domain.Profile;
import me.donas.boost.domain.user.domain.Role;
import me.donas.boost.domain.user.domain.User;
import me.donas.boost.domain.user.domain.UserPrincipal;
import me.donas.boost.domain.user.dto.LoginRequest;
import me.donas.boost.domain.user.dto.LogoutRequest;
import me.donas.boost.domain.user.dto.ProfileResponse;
import me.donas.boost.domain.user.dto.ProfileUpdateRequest;
import me.donas.boost.domain.user.dto.RefreshRequest;
import me.donas.boost.domain.user.dto.SignupRequest;
import me.donas.boost.domain.user.dto.TokenResponse;
import me.donas.boost.domain.user.dto.UpdatePasswordRequest;
import me.donas.boost.domain.user.dto.UserInfoResponse;

public class UserFixture {
	public static final Long USER_ID = 1L;
	public static final String USERNAME = "username";
	public static final String EMAIL = "proto_seo@naver.com";
	public static final String NICKNAME = "proto_seo@naver.com";
	public static final String IMAGE = "https://test-bucket.s3.amazonaws.com/username/profile.jpg";
	public static final String PASSWORD = "password";
	public static final String UPDATE_PASSWORD = "updatePassword";
	public static final String INTRODUCE = "introduce";
	public static final LocalDate BIRTH_DAY = LocalDate.of(1993, 12, 16);
	public static final Gender GENDER = Gender.MALE;
	public static final boolean AGREE_PROMOTION = true;
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String REFRESH_TOKEN = UUID.randomUUID().toString();

	public static User user() {
		User user = User.builder()
			.username(USERNAME)
			.email(EMAIL)
			.profile(profile())
			.password(PASSWORD)
			.birthDay(BIRTH_DAY)
			.gender(GENDER)
			.agreePromotion(AGREE_PROMOTION)
			.role(Role.ROLE_USER)
			.build();
		ReflectionTestUtils.setField(user, "id", USER_ID);
		return user;
	}

	public static UserPrincipal userPrincipal() {
		return UserPrincipal.create(user());
	}

	public static Profile profile() {
		return Profile.builder().nickname(NICKNAME).image(IMAGE).introduce(INTRODUCE).build();
	}

	public static UserInfoResponse userInfoResponse() {
		return UserInfoResponse.of(user());
	}

	public static SignupRequest signupRequest() {
		return new SignupRequest(USERNAME, PASSWORD, EMAIL, NICKNAME, GENDER, BIRTH_DAY, AGREE_PROMOTION);
	}

	public static LoginRequest loginRequest() {
		return new LoginRequest(USERNAME, PASSWORD);
	}

	public static TokenResponse tokenResponse() {
		return new TokenResponse(ACCESS_TOKEN, REFRESH_TOKEN);
	}

	public static LogoutRequest logoutRequest() {
		return new LogoutRequest(USERNAME);
	}

	public static RefreshRequest refreshRequest() {
		return new RefreshRequest(USERNAME, REFRESH_TOKEN);
	}

	public static ProfileResponse profileResponse() {
		return new ProfileResponse(NICKNAME, INTRODUCE, IMAGE);
	}

	public static ProfileUpdateRequest profileUpdateRequest() {
		return new ProfileUpdateRequest(NICKNAME, INTRODUCE);
	}

	public static UpdatePasswordRequest updatePasswordRequest() {
		return new UpdatePasswordRequest(PASSWORD, UPDATE_PASSWORD);
	}
}
