package me.donas.boost.domain.schedule.dto;

import java.time.LocalDateTime;

import me.donas.boost.domain.schedule.domain.Schedule;

public record ScheduleSimpleResponse(
	Long scheduleId,
	String title,
	String bannerImage,
	String description,
	LocalDateTime scheduledTime
) {
	public static ScheduleSimpleResponse of(Schedule schedule) {
		return new ScheduleSimpleResponse(
			schedule.getId(),
			schedule.getTitle(),
			schedule.getBannerImage(),
			schedule.getDescription(),
			schedule.getScheduledTime()
		);
	}
}
