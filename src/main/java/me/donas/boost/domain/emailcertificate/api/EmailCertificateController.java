package me.donas.boost.domain.emailcertificate.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.emailcertificate.application.EmailCertificateServiceDecorator;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateConfirmRequest;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateIssueRequest;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmailCertificateController {

	private final EmailCertificateServiceDecorator emailCertificateService;

	@PostMapping("/email-certificates")
	public ResponseEntity<EmailCertificateResponse> issueCertifyEmail(
		@RequestBody @Valid EmailCertificateIssueRequest request) {
		EmailCertificateResponse response = emailCertificateService.issueCertifyEmail(request);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/email-certificates")
	public ResponseEntity<Void> confirmCertifyEmail(@RequestBody @Valid EmailCertificateConfirmRequest request) {
		if (emailCertificateService.confirmCertifyEmail(request)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}
}
