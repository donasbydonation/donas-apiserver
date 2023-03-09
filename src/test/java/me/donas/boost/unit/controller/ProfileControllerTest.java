package me.donas.boost.unit.controller;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;
import static me.donas.boost.global.exception.CommonErrorCode.*;
import static me.donas.boost.unit.fixture.UserFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.donas.boost.domain.user.api.ProfileController;
import me.donas.boost.domain.user.application.ProfileService;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.global.exception.FileException;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest extends ControllerTest {
	@MockBean
	private ProfileService profileService;

	@Test
	@DisplayName("프로필 불러오기 테스트")
	void getProfileTest() throws Exception {
		given(profileService.getProfile(USERNAME)).willReturn(profileResponse());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/profile/{username}", USERNAME)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
					pathParameters(usernamePathParameters),
					responseFields(profileRequestFields)
				)
			);
	}

	@Test
	@DisplayName("프로필 불러오기 실패 테스트")
	void getProfileFailTest() throws Exception {
		given(profileService.getProfile(USERNAME)).willThrow(new UserException(NOTFOUND));

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/profile/{username}", USERNAME)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isNotFound())
			.andDo(restDocs.document(
					pathParameters(usernamePathParameters),
					responseFields(exceptionResponseFields)
				)
			);
	}

	@Test
	@DisplayName("프로필 변경 테스트")
	void updateProfileTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(profileUpdateRequest());
		MockMultipartFile request = new MockMultipartFile("profileData", "", MediaType.APPLICATION_JSON_VALUE,
			content.getBytes());
		MockMultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE,
			new byte[1024 * 1024 * 10]);
		given(profileService.updateProfile(any(), eq(profileUpdateRequest()), eq(image))).willReturn(profileResponse());

		MockMultipartHttpServletRequestBuilder builder = getMockMultipartHttpServletRequestBuilder();

		ResultActions result = mockMvc.perform(
			builder.file(image).file(request)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
					requestPartFields("profileData",
						fieldWithPath("nickname").description("닉네임"),
						fieldWithPath("introduce").description("자기소개")),
					responseFields(profileRequestFields)
				)
			);
	}

	@Test
	@DisplayName("프로필 변경 실패 테스트(용량 초과)")
	void updateProfileFailWithLimitSizeOverTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(profileUpdateRequest());
		MockMultipartFile request = new MockMultipartFile("profileData", "", MediaType.APPLICATION_JSON_VALUE,
			content.getBytes());
		MockMultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE,
			new byte[1024 * 1024 * 20]);
		given(profileService.updateProfile(any(), eq(profileUpdateRequest()), eq(image))).willThrow(
			new FileException(PAYLOAD_TOO_LARGE));

		MockMultipartHttpServletRequestBuilder builder = getMockMultipartHttpServletRequestBuilder();

		ResultActions result = mockMvc.perform(builder.file(image).file(request).accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isPayloadTooLarge())
			.andDo(restDocs.document(
					requestPartFields("profileData",
						fieldWithPath("nickname").description("닉네임"),
						fieldWithPath("introduce").description("자기소개")),
					responseFields(exceptionResponseFields)
				)
			);
	}

	private MockMultipartHttpServletRequestBuilder getMockMultipartHttpServletRequestBuilder() {
		MockMultipartHttpServletRequestBuilder builder = RestDocumentationRequestBuilders.multipart("/api/v1/profile");

		builder.with(req -> {
			req.setMethod("PUT");
			return req;
		});

		return builder;
	}
}