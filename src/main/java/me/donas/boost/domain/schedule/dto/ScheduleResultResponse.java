package me.donas.boost.domain.schedule.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record ScheduleResultResponse(
	Long creatorId,
	String creatorName,
	String profileImage,
	List<PlatformResultResponse> platforms,
	String title,
	String bannerImage,
	String description,
	ZonedDateTime scheduledTime
) {
	public static ScheduleResultResponse of(ScheduleQueryResponse scheduleQueryResponse,
		List<PlatformResultResponse> platformResultResponses) {
		return new ScheduleResultResponse(
			scheduleQueryResponse.getCreatorId(),
			scheduleQueryResponse.getCreatorName(),
			scheduleQueryResponse.getProfileImage(),
			platformResultResponses,
			scheduleQueryResponse.getTitle(),
			scheduleQueryResponse.getBannerImage(),
			scheduleQueryResponse.getDescription(),
			scheduleQueryResponse.getScheduledTime()
		);
	}
}
