package me.donas.boost.domain.emailcertificate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record EmailCertificateIssueRequest(@NotEmpty @Email String email) {
}
