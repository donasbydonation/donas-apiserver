package me.donas.boost.domain.emailcertificate.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmailCertificate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String email;

	String authenticationKey;

	boolean isCertificated;

	LocalDateTime expiredDate;

	@Builder
	public EmailCertificate(Long id, String email, String authenticationKey, boolean isCertificated,
		LocalDateTime expiredDate) {
		this.id = id;
		this.email = email;
		this.authenticationKey = authenticationKey;
		this.isCertificated = isCertificated;
		this.expiredDate = expiredDate;
	}

	public static EmailCertificate create(String email, String authenticationKey) {
		return EmailCertificate.builder()
			.email(email)
			.authenticationKey(authenticationKey)
			.isCertificated(false)
			.expiredDate(LocalDateTime.now().plusMinutes(10))
			.build();
	}

	public boolean certify(String authenticationKey) {
		if (this.authenticationKey.equals(authenticationKey)) {
			this.isCertificated = true;
			return true;
		}
		return false;
	}
}
