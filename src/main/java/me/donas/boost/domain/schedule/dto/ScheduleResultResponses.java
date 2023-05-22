package me.donas.boost.domain.schedule.dto;

import java.util.ArrayList;
import java.util.List;

public record ScheduleResultResponses(
	int totalPage,
	int recommendPage,
	int nowPage,
	List<ScheduleResultResponse> schedules
) {
	public static ScheduleResultResponses empty() {
		return new ScheduleResultResponses(0, 0, 0, new ArrayList<>());
	}
}
