package me.donas.boost.unit.controller;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;
import static me.donas.boost.unit.fixture.UserFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import me.donas.boost.domain.user.api.AuthController;
import me.donas.boost.domain.user.application.AuthService;
import me.donas.boost.domain.user.dto.LoginRequest;
import me.donas.boost.domain.user.dto.LogoutRequest;
import me.donas.boost.domain.user.dto.RefreshRequest;
import me.donas.boost.domain.user.dto.SignupRequest;
import me.donas.boost.domain.user.exception.UserException;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTest {
	@MockBean
	protected AuthService authService;

	@DisplayName("회원가입 성공 테스트")
	@Test
	void signupSuccessTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		given(authService.register(any(SignupRequest.class))).willReturn(USER_ID);
		String content = objectMapper.writeValueAsString(signupRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/signup")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isCreated())
			.andDo(restDocs.document(
					requestFields(userRequestFields)
				)
			);
	}

	@DisplayName("회원가입 실패 테스트(username 중복)")
	@Test
	void signupFailWithDuplicateUsernameTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		given(authService.register(any(SignupRequest.class))).willThrow(new UserException(DUPLICATE_USERNAME));
		String content = objectMapper.writeValueAsString(signupRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/signup")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isConflict())
			.andDo(restDocs.document(
					requestFields(userRequestFields),
					responseFields(exceptionResponseFields)
				)
			);
	}

	@DisplayName("회원가입 실패 테스트(nickname 중복)")
	@Test
	void signupFailWithDuplicateNicknameTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		given(authService.register(any(SignupRequest.class))).willThrow(new UserException(DUPLICATE_NICKNAME));
		String content = objectMapper.writeValueAsString(signupRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/signup")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isConflict())
			.andDo(restDocs.document(
					requestFields(userRequestFields),
					responseFields(exceptionResponseFields)
				)
			);
	}

	@DisplayName("로그인 성공 테스트")
	@Test
	void loginSuccessTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		given(authService.login(any(LoginRequest.class))).willReturn(tokenResponse());
		String content = objectMapper.writeValueAsString(loginRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/login")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
					requestFields(loginRequestFields),
					responseFields(tokenResponseFields)
				)
			);
	}

	@DisplayName("로그인 실패 테스트")
	@Test
	void loginFailTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		given(authService.login(any(LoginRequest.class))).willThrow(new UserException(INVALID_PASSWORD));
		String content = objectMapper.writeValueAsString(loginRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/login")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isUnauthorized())
			.andDo(restDocs.document(
					requestFields(loginRequestFields),
					responseFields(exceptionResponseFields)
				)
			);
	}

	@DisplayName("로그아웃 성공 테스트")
	@Test
	void logoutSuccessTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		willDoNothing().given(authService).logout(any(LogoutRequest.class));
		String content = objectMapper.writeValueAsString(logoutRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/logout")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
					requestFields(
						fieldWithPath("username").type(STRING).description("아이디")
					)
				)
			);
	}

	@DisplayName("토큰 리프레시 성공 테스트")
	@Test
	void refreshSuccessTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		given(authService.refresh(refreshRequest())).willReturn(tokenResponse());
		String content = objectMapper.writeValueAsString(refreshRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/refresh")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
					requestFields(refreshRequestFields),
					responseFields(tokenResponseFields)
				)
			);
	}

	@DisplayName("토큰 리프레시 실패 테스트")
	@Test
	void refreshFailTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		given(authService.refresh(any(RefreshRequest.class))).willThrow(new UserException(EXPIRED_REFRESH_TOKEN));
		String content = objectMapper.writeValueAsString(refreshRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/refresh")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isUnauthorized())
			.andDo(restDocs.document(
					requestFields(refreshRequestFields),
					responseFields(exceptionResponseFields)
				)
			);
	}
}