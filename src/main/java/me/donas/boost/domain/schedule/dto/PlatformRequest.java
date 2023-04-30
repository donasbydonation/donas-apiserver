package me.donas.boost.domain.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.Platform;
import me.donas.boost.domain.schedule.domain.PlatformProvider;

public record PlatformRequest(@NotBlank String platform, @NotBlank String broadcastLink) {
	public Platform toEntity(CreatorInfo creatorInfo) {
		return Platform.builder()
			.provider(PlatformProvider.valueOf(platform.toUpperCase()))
			.creatorInfo(creatorInfo)
			.broadcastLink(broadcastLink)
			.build();
	}
}
