package me.donas.boost.domain.emailcertificate.application;

import static me.donas.boost.domain.emailcertificate.exception.EmailCertificateErrorCode.*;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.emailcertificate.domain.EmailCertificate;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateConfirmRequest;
import me.donas.boost.domain.emailcertificate.dto.EmailCertificateIssueRequest;
import me.donas.boost.domain.emailcertificate.exception.EmailCertificateException;
import me.donas.boost.domain.emailcertificate.repository.EmailCertificateRepository;
import me.donas.boost.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class EmailCertificateService {
	private final EmailCertificateRepository emailCertificateRepository;
	private final UserRepository userRepository;
	private final JavaMailSender mailSender;
	private static final String MAIL_SUBJECT = "Donas 회원가입 인증 코드";

	@Transactional
	public EmailCertificate issueCertifyEmail(EmailCertificateIssueRequest request) {
		if (isExistsByEmail(request.email())) {
			throw new EmailCertificateException(DUPLICATE_EMAIL);
		}
		String authenticationKey = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 999999));
		return emailCertificateRepository.save(EmailCertificate.create(request.email(), authenticationKey));
	}

	private boolean isExistsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Async
	public void sendAuthenticationKeyToEmail(String email, String authenticationKey) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject(MAIL_SUBJECT);
		message.setText(authenticationKey);
		mailSender.send(message);
	}

	@Transactional
	public boolean confirmCertifyEmail(EmailCertificateConfirmRequest request) {
		EmailCertificate emailCertification = emailCertificateRepository.findById(request.id())
			.orElseThrow(() -> new EmailCertificateException(REQUEST_NOT_FOUND));

		if (emailCertification.getExpiredDate().isBefore(LocalDateTime.now())) {
			throw new EmailCertificateException(CERTIFY_TIMEOUT);
		}
		if (emailCertification.isCertificated()) {
			throw new EmailCertificateException(ALREADY_CERTIFIED);
		}
		return emailCertification.certify(request.authenticationKey());
	}
}
