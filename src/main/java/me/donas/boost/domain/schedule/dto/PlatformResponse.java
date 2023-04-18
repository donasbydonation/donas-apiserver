package me.donas.boost.domain.schedule.dto;

import me.donas.boost.domain.schedule.domain.Platform;
import me.donas.boost.domain.schedule.domain.PlatformProvider;

public record PlatformResponse(Long platformId, PlatformProvider provider, String broadcastLink) {
	public static PlatformResponse of(Platform platform) {
		return new PlatformResponse(platform.getId(), platform.getProvider(), platform.getBroadcastLink());
	}
}
