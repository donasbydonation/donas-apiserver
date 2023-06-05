package me.donas.boost.domain.schedule.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.application.ScheduleServiceV2;
import me.donas.boost.domain.schedule.domain.PlatformProvider;
import me.donas.boost.domain.schedule.dto.ScheduleRequest;
import me.donas.boost.domain.schedule.dto.ScheduleResultResponses;
import me.donas.boost.global.exception.CommonErrorCode;
import me.donas.boost.global.exception.FileException;

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

	@PostMapping("/schedules/excel")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Long> updateSchedule(
		@RequestPart(value = "excel") MultipartFile file
	) throws IOException {
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!"xlsx".equals(extension)) {
			throw new FileException(CommonErrorCode.INVALID_FILE_TYPE);
		}
		Workbook workbook = new XSSFWorkbook(file.getInputStream());

		Sheet worksheet = workbook.getSheetAt(0);
		List<ScheduleRequest> scheduleRequests = new ArrayList<>();
		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
			Row row = worksheet.getRow(i);
			if (row.getCell(0) == null) {
				break;
			}
			if (row.getCell(5) != null) {
				continue;
			}
			scheduleRequests.add(extractScheduleInformation(row));
		}
		scheduleService.createSchedule(scheduleRequests);
		return ResponseEntity.ok().build();
	}

	private ScheduleRequest extractScheduleInformation(Row row) {
		Long creatorId = (long)row.getCell(0).getNumericCellValue();
		String title = row.getCell(1).getStringCellValue();
		String description = row.getCell(2).getStringCellValue();
		ZonedDateTime scheduledTime = createdScheduleTime(
			(int)row.getCell(3).getNumericCellValue(),
			(int)row.getCell(4).getNumericCellValue()
		);
		return new ScheduleRequest(creatorId, title, description, scheduledTime);
	}

	private ZonedDateTime createdScheduleTime(int date, int time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmm");
		StringBuilder dateTime = new StringBuilder(String.format("%08d ", date))
			.append(String.format("%04d", time));
		LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);

		return ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"))
			.withZoneSameInstant(ZoneId.of("UTC"));
	}
}
