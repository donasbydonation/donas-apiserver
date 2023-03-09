package me.donas.boost.unit.controller;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;
import static me.donas.boost.unit.fixture.UserFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.donas.boost.domain.user.api.UserController;
import me.donas.boost.domain.user.application.UserService;
import me.donas.boost.domain.user.exception.UserException;

@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTest {
	@MockBean
	protected UserService userService;

	@DisplayName("유저 개인정보 반환")
	@Test
	void getUserInformationTest() throws Exception {
		given(userService.getUserInformation(any(), eq(USERNAME))).willReturn(userInfoResponse());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/users/{username}", USERNAME)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
					pathParameters(usernamePathParameters),
					responseFields(userInfoResponseFields)
				)
			);
	}

	@DisplayName("비밀번호 변경 성공 테스트")
	@Test
	void updateUserPasswordTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		willDoNothing().given(userService).updatePassword(userPrincipal(), USERNAME, updatePasswordRequest());
		String content = objectMapper.writeValueAsString(updatePasswordRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/users/{username}/password", USERNAME)
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(usernamePathParameters))
			);
	}

	@DisplayName("비밀번호 변경 실패 테스트")
	@Test
	void updateUserPasswordFailTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		willThrow(new UserException(INVALID_PASSWORD)).given(userService)
			.updatePassword(any(), eq(USERNAME), eq(updatePasswordRequest()));
		String content = objectMapper.writeValueAsString(updatePasswordRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/users/{username}/password", USERNAME)
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isUnauthorized())
			.andDo(restDocs.document(
					pathParameters(usernamePathParameters),
					responseFields(exceptionResponseFields)
				)
			);
	}

	@DisplayName("회원 탈퇴 성공 테스트")
	@Test
	void withdrawUserTest() throws Exception {
		willDoNothing().given(userService).withdraw(userPrincipal(), USERNAME);

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/api/v1/users/{username}", USERNAME)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
					pathParameters(usernamePathParameters)
				)
			);
	}

	@DisplayName("회원 탈퇴 실패 테스트")
	@Test
	void withdrawUserFailTest() throws Exception {
		willThrow(new UserException(INVALID_PASSWORD)).given(userService).withdraw(any(), eq(USERNAME));

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/api/v1/users/{username}", USERNAME)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isUnauthorized())
			.andDo(restDocs.document(
					pathParameters(usernamePathParameters),
					responseFields(exceptionResponseFields)
				)
			);
	}
}