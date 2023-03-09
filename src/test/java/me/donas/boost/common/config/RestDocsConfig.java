package me.donas.boost.common.config;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

@TestConfiguration
public class RestDocsConfig {

	@Bean
	public RestDocumentationResultHandler restDocumentationResultHandler() {
		return MockMvcRestDocumentation.document(
			"{ClassName}/{methodName}",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint())
		);
	}
}
