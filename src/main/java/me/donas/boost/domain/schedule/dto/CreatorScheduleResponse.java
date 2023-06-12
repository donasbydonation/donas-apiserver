package me.donas.boost.domain.schedule.dto;

import java.util.List;

import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.Schedule;

public record CreatorScheduleResponse(
	Long creatorId,
	String creatorName,
	String profileImage,
	List<PlatformResultResponse> platforms,
	List<ScheduleSimpleResponse> schedules
) {
	public static CreatorScheduleResponse of(CreatorInfo creatorInfo, List<Schedule> schedules) {
		return new CreatorScheduleResponse(
			creatorInfo.getId(),
			creatorInfo.getName(),
			creatorInfo.getProfileImage(),
			creatorInfo.getPlatforms().stream().map(PlatformResultResponse::of).toList(),
			schedules.stream().map(ScheduleSimpleResponse::of).toList()
		);
	}
}
