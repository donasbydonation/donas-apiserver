package me.donas.boost.domain.schedule.dto;

import java.util.ArrayList;
import java.util.List;

public record ScheduleQueryResponses(
	long totalPage,
	long recommendPage,
	List<ScheduleQueryResponse> schedules
) {
	public static ScheduleQueryResponses of(long totalPage, long recommendPage, List<ScheduleQueryResponse> schedules) {
		return new ScheduleQueryResponses(totalPage, recommendPage, schedules);
	}

	public static ScheduleQueryResponses empty() {
		return new ScheduleQueryResponses(0, 0, new ArrayList<>());
	}
}
