package me.donas.boost.unit.fixture;

import java.time.LocalDateTime;

import me.donas.boost.domain.emailcertificate.dto.EmailCertificateConfirmRequest;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateIssueRequest;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateResponse;

public class EmailFixture {
	public static final String EMAIL = "proto_seo@naver.com";
	public static final Long CERTIFICATE_ID = 1L;
	public static final String AUTHENTICATION_KEY = "123456";
	public static final LocalDateTime EXPIRED_DATE = LocalDateTime.now().plusMinutes(10);

	public static EmailCertificateIssueRequest emailCertificateIssueRequest() {
		return new EmailCertificateIssueRequest(EMAIL);
	}

	public static EmailCertificateResponse emailCertificateResponse() {
		return new EmailCertificateResponse(CERTIFICATE_ID, EXPIRED_DATE);
	}

	public static EmailCertificateConfirmRequest emailCertificateConfirmRequest() {
		return new EmailCertificateConfirmRequest(CERTIFICATE_ID, AUTHENTICATION_KEY);
	}
}
