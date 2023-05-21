package me.donas.boost.domain.schedule.api;

import java.time.ZonedDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.application.ScheduleServiceV2;
import me.donas.boost.domain.schedule.domain.PlatformProvider;
import me.donas.boost.domain.schedule.dto.ScheduleResultResponses;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class ScheduleControllerV2 {
	private final ScheduleServiceV2 scheduleService;

	@GetMapping("/schedules")
	public ResponseEntity<ScheduleResultResponses> readSchedules(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime time,
		@RequestParam(defaultValue = "0") int day,
		@RequestParam(defaultValue = "TOTAL") PlatformProvider provider,
		@RequestParam(defaultValue = "-1") int page,
		@RequestParam(defaultValue = "15") int size
	) {
		return ResponseEntity.ok(scheduleService.readSchedules(time, day, provider, page, size));
	}
}
