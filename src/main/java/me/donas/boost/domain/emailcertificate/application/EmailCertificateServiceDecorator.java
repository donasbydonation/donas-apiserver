package me.donas.boost.domain.emailcertificate.application;

import static me.donas.boost.domain.emailcertificate.exception.EmailCertificateErrorCode.*;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.emailcertificate.domain.EmailCertificate;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateConfirmRequest;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateIssueRequest;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateResponse;
import me.donas.boost.domain.emailcertificate.exception.EmailCertificateException;

@Component
@RequiredArgsConstructor
public class EmailCertificateServiceDecorator {
	private final EmailCertificateService emailCertificateService;

	public EmailCertificateResponse issueCertifyEmail(EmailCertificateIssueRequest request) {
		EmailCertificate emailCertification = emailCertificateService.issueCertifyEmail(request);
		emailCertificateService.sendAuthenticationKeyToEmail(request.email(), emailCertification.getAuthenticationKey());
		return new EmailCertificateResponse(emailCertification.getId(), emailCertification.getExpiredDate());
	}

	public boolean confirmCertifyEmail(EmailCertificateConfirmRequest request) {
		if (!emailCertificateService.confirmCertifyEmail(request)) {
			throw new EmailCertificateException(INCORRECT_CODE);
		}
		return true;
	}
}
