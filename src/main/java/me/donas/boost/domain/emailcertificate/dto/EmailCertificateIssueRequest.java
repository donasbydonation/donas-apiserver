package me.donas.boost.domain.emailcertificate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailCertificateIssueRequest(@NotBlank @Email String email) {
}
