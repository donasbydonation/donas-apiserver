package me.donas.boost.domain.schedule.dto;

import me.donas.boost.domain.schedule.domain.PlatformProvider;

public record PlatformResultResponse(
	PlatformProvider provider,
	String broadcastLink
) {
	public static PlatformResultResponse of(PlatformProvider provider, String broadcastLink) {
		return new PlatformResultResponse(provider, broadcastLink);
	}
}
