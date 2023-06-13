package me.donas.boost.domain.schedule.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import me.donas.boost.domain.schedule.domain.Schedule;

public record ScheduleSimpleResponse(
	Long scheduleId,
	String title,
	String bannerImage,
	String description,
	ZonedDateTime scheduledTime
) {
	public static ScheduleSimpleResponse of(Schedule schedule) {
		return new ScheduleSimpleResponse(
			schedule.getId(),
			schedule.getTitle(),
			schedule.getBannerImage(),
			schedule.getDescription(),
			schedule.getScheduledTime().atZone(ZoneId.of("UTC"))
		);
	}
}
