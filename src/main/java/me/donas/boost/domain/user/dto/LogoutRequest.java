package me.donas.boost.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(@NotBlank String username) {
}
