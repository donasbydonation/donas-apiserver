package me.donas.boost.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(@NotBlank String password, @NotBlank String updatePassword) {
}
