package me.donas.boost.domain.emailcertificate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmailCertificateConfirmRequest(@NotNull Long id, @NotBlank String authenticationKey) {
}
