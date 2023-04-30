package me.donas.boost.domain.schedule.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import me.donas.boost.domain.schedule.domain.CreatorInfo;

public record CreatorInfoRequest(
	@NotBlank String name,
	@NotEmpty List<PlatformRequest> platforms
) {
	public CreatorInfo toEntity(String profileImage) {
		return CreatorInfo.builder()
			.name(name)
			.profileImage(profileImage)
			.platforms(new ArrayList<>())
			.build();
	}
}