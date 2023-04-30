package me.donas.boost.domain.emailcertificate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.donas.boost.domain.emailcertificate.domain.EmailCertificate;

public interface EmailCertificateRepository extends JpaRepository<EmailCertificate, Long> {
}
