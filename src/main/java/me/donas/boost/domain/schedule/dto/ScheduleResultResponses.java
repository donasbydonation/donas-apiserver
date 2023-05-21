package me.donas.boost.domain.schedule.dto;

import java.util.List;

public record ScheduleResultResponses(
	int totalPage,
	int recommendPage,
	List<ScheduleResultResponse> schedules
) {
}
