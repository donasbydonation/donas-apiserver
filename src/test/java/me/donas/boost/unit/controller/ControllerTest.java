package me.donas.boost.unit.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import me.donas.boost.common.config.RestDocsConfig;
import me.donas.boost.global.security.JwtService;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
public abstract class ControllerTest {
	@MockBean
	protected JwtService jwtService;

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	protected final FieldDescriptor[] exceptionResponseFields = new FieldDescriptor[] {
		fieldWithPath("name").type(STRING).description("예외 이름"),
		fieldWithPath("httpStatus").type(STRING).description("HTTP 상태 코드"),
		fieldWithPath("message").type(STRING).description("예외 내용")};

	protected final FieldDescriptor[] userRequestFields = new FieldDescriptor[] {
		fieldWithPath("username").type(STRING).description("아이디"),
		fieldWithPath("password").type(STRING).description("비밀번호"),
		fieldWithPath("email").type(STRING).description("이메일"),
		fieldWithPath("nickname").type(STRING).description("닉네임"),
		fieldWithPath("gender").type(STRING).description("성별"),
		fieldWithPath("birthDay").type(STRING).description("생일"),
		fieldWithPath("agreePromotion").type(BOOLEAN).description("프로모션 동의여부")
	};

	protected final FieldDescriptor[] loginRequestFields = new FieldDescriptor[] {
		fieldWithPath("username").type(STRING).description("아이디"),
		fieldWithPath("password").type(STRING).description("비밀번호")
	};

	protected final FieldDescriptor[] tokenResponseFields = new FieldDescriptor[] {
		fieldWithPath("accessToken").type(STRING).description("엑세스 토큰"),
		fieldWithPath("refreshToken").type(STRING).description("리프레시 토큰")
	};

	protected final FieldDescriptor[] refreshRequestFields = new FieldDescriptor[] {
		fieldWithPath("username").type(STRING).description("아이디"),
		fieldWithPath("refreshToken").type(STRING).description("리프레시 토큰")
	};

	protected final FieldDescriptor[] profileRequestFields = new FieldDescriptor[] {
		fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
		fieldWithPath("introduce").type(JsonFieldType.STRING).description("자기소개"),
		fieldWithPath("image").type(JsonFieldType.STRING).description("이미지")
	};

	protected final FieldDescriptor[] userInfoResponseFields = new FieldDescriptor[] {
		fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
		fieldWithPath("birthDay").type(JsonFieldType.STRING).description("생일"),
		fieldWithPath("agreePromotion").type(JsonFieldType.BOOLEAN).description("프로모션 동의여부")
	};

	protected final ParameterDescriptor[] usernamePathParameters = new ParameterDescriptor[] {
		parameterWithName("username").description("아이디")
	};

	@BeforeEach
	void setUp(RestDocumentationContextProvider provider) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
			.apply(documentationConfiguration(provider))
			.alwaysDo(restDocs)
			.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
			.build();
	}
}
