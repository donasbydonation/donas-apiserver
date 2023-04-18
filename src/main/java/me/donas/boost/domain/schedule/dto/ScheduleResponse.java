package me.donas.boost.domain.schedule.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import me.donas.boost.domain.schedule.domain.Schedule;

public record ScheduleResponse(
	Long scheduleId,
	Long creatorId,
	String creatorName,
	String profileImage,
	List<PlatformResponse> platforms,
	String title,
	String bannerImage,
	String description,
	ZonedDateTime scheduledTime
) {
	public static ScheduleResponse of(Schedule schedule) {
		return new ScheduleResponse(
			schedule.getId(),
			schedule.getCreatorInfo().getId(),
			schedule.getCreatorInfo().getName(),
			schedule.getCreatorInfo().getProfileImage(),
			schedule.getCreatorInfo().getPlatforms().stream().map(PlatformResponse::of).toList(),
			schedule.getTitle(),
			schedule.getBannerImage(),
			schedule.getDescription(),
			schedule.getScheduledTime().atZone(ZoneId.of("UTC"))
		);
	}
}
