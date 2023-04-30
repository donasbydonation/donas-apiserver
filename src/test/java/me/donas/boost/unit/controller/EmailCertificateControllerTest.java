package me.donas.boost.unit.controller;

import static me.donas.boost.domain.emailcertificate.exception.EmailCertificateErrorCode.*;
import static me.donas.boost.unit.fixture.EmailFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.donas.boost.domain.emailcertificate.api.EmailCertificateController;
import me.donas.boost.domain.emailcertificate.application.EmailCertificateServiceDecorator;
import me.donas.boost.domain.emailcertificate.exception.EmailCertificateException;

@WebMvcTest(EmailCertificateController.class)
class EmailCertificateControllerTest extends ControllerTest {
	@MockBean
	private EmailCertificateServiceDecorator emailCertificateService;

	@Test
	@DisplayName("이메일 인증 코드 전송 성공")
	void createEmailCertificationTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		given(emailCertificateService.issueCertifyEmail(emailCertificateIssueRequest())).willReturn(
			emailCertificateResponse());
		String content = objectMapper.writeValueAsString(emailCertificateIssueRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/email-certificates")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
					requestFields(
						fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
					),
					responseFields(
						fieldWithPath("certificationId").type(JsonFieldType.NUMBER).description("인증코드 아이디"),
						fieldWithPath("expiredDate").type(JsonFieldType.STRING).description("만료기간")
					)
				)
			);
	}

	@Test
	@DisplayName("이메일 인증 코드 검증 성공")
	void emailCertificateSuccessTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		given(emailCertificateService.confirmCertifyEmail(emailCertificateConfirmRequest())).willReturn(true);
		String content = objectMapper.writeValueAsString(emailCertificateConfirmRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/email-certificates")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isOk())
			.andDo(restDocs.document(
					requestFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("인증코드 아이디"),
						fieldWithPath("authenticationKey").type(JsonFieldType.STRING).description("인증 코드")
					)
				)
			);
	}

	@Test
	@DisplayName("이메일 인증 코드 검증 실패")
	void emailCertificateFailTest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		given(emailCertificateService.confirmCertifyEmail(emailCertificateConfirmRequest()))
			.willThrow(new EmailCertificateException(INCORRECT_CODE));
		String content = objectMapper.writeValueAsString(emailCertificateConfirmRequest());

		ResultActions result = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/email-certificates")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result
			.andExpect(status().isBadRequest())
			.andDo(restDocs.document(
					responseFields(exceptionResponseFields)
				)
			);
	}
}