package me.donas.boost.domain.schedule.api;

import java.net.URI;
import java.time.ZonedDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.application.ScheduleService;
import me.donas.boost.domain.schedule.domain.PlatformProvider;
import me.donas.boost.domain.schedule.dto.ScheduleRequest;
import me.donas.boost.domain.schedule.dto.ScheduleUpdateRequest;
import me.donas.boost.domain.schedule.dto.SchedulesResponse;
import me.donas.boost.domain.schedule.dto.SchedulesTotalResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ScheduleController {
	private final ScheduleService scheduleService;

	@GetMapping("/schedules")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<SchedulesResponse> readSchedules(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(scheduleService.readSchedulesFromAdmin(page, size));
	}

	@PostMapping("/schedules")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> createSchedule(
		@Valid @RequestPart(value = "schedule") ScheduleRequest request,
		@RequestPart(value = "banner", required = false) MultipartFile file
	) {
		Long id = scheduleService.createSchedule(request, file);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/schedules/{id}")
			.buildAndExpand(id).toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping("/schedules")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Long> updateSchedule(
		@Valid @RequestPart(value = "schedule") ScheduleUpdateRequest request,
		@RequestPart(value = "banner", required = false) MultipartFile file
	) {
		Long id = scheduleService.updateSchedule(request, file);
		return ResponseEntity.ok(id);
	}

	@DeleteMapping("/schedules/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Long> deleteSchedule(@PathVariable Long id) {
		return ResponseEntity.ok(scheduleService.deleteSchedule(id));
	}

	@GetMapping("/schedules/list")
	public ResponseEntity<SchedulesTotalResponse> readSchedules(
		@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) ZonedDateTime time,
		@RequestParam(defaultValue = "0") int day,
		@RequestParam(defaultValue = "TOTAL") PlatformProvider provider,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "5") int size
	) {
		return ResponseEntity.ok(scheduleService.readSchedules(time, day, provider, page, size));
	}
}
