package me.donas.boost.domain.schedule.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreatorInfoUpdateRequest(
	@NotNull Long id,
	@NotBlank String name,
	@NotEmpty List<PlatformRequest> platforms
) {
}
