package me.donas.boost.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateProductRequest(@NotBlank String name, @Positive int count, @Positive long price) {
}
