package me.donas.boost.domain.emailcertificate.dto;

import java.time.LocalDateTime;

public record EmailCertificateResponse(Long certificationId, LocalDateTime expiredDate) {
}
