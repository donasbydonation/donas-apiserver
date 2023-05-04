package me.donas.boost.domain.schedule.dto;

import java.util.Map;

import me.donas.boost.domain.schedule.domain.PlatformProvider;

public record SchedulesTotalResponse(
	ScheduleQueryResponses twitch,
	ScheduleQueryResponses afreeca,
	ScheduleQueryResponses youtube
) {
	public static SchedulesTotalResponse of(Map<PlatformProvider, ScheduleQueryResponses> map) {
		return new SchedulesTotalResponse(
			map.getOrDefault(PlatformProvider.TWITCH, ScheduleQueryResponses.empty()),
			map.getOrDefault(PlatformProvider.AFREECA, ScheduleQueryResponses.empty()),
			map.getOrDefault(PlatformProvider.YOUTUBE, ScheduleQueryResponses.empty())
		);
	}
}
