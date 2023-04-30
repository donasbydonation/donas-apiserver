package me.donas.boost.domain.schedule.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScheduleUpdateRequest(
	@NotNull
	Long scheduleId,
	@NotNull
	Long creatorId,
	@NotBlank
	String title,
	@NotBlank
	String description,
	@NotNull
	LocalDateTime scheduledTime
) {
}
