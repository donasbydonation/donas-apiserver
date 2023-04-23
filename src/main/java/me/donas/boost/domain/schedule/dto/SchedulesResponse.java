package me.donas.boost.domain.schedule.dto;

import java.util.List;

public record SchedulesResponse(
	long totalPage,
	List<ScheduleResponse> schedules
) {
	public static SchedulesResponse of(long totalPage, List<ScheduleResponse> schedules) {
		return new SchedulesResponse(totalPage, schedules);
	}
}
