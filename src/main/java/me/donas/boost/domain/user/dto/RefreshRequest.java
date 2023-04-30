package me.donas.boost.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank String username, @NotBlank String refreshToken) {
}
