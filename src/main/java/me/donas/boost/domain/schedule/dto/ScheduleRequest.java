package me.donas.boost.domain.schedule.dto;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.Schedule;

public record ScheduleRequest(
	@NotNull
	Long creatorId,
	@NotBlank
	String title,
	@NotBlank
	String description,
	@NotNull
	ZonedDateTime scheduledTime
) {
	public Schedule toEntity(CreatorInfo creatorInfo, String bannerImage) {
		return Schedule.builder()
			.creatorInfo(creatorInfo)
			.title(title)
			.bannerImage(bannerImage)
			.description(description)
			.scheduledTime(scheduledTime.toLocalDateTime())
			.build();
	}
}