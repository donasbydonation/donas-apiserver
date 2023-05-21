package me.donas.boost.domain.schedule.application;

import static me.donas.boost.domain.schedule.exception.CreatorInfoErrorCode.*;
import static me.donas.boost.domain.schedule.exception.ScheduleErrorCode.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneOffsetTransition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.PlatformProvider;
import me.donas.boost.domain.schedule.domain.Schedule;
import me.donas.boost.domain.schedule.dto.ScheduleQueryResponse;
import me.donas.boost.domain.schedule.dto.ScheduleQueryResponses;
import me.donas.boost.domain.schedule.dto.ScheduleRequest;
import me.donas.boost.domain.schedule.dto.ScheduleResponse;
import me.donas.boost.domain.schedule.dto.ScheduleUpdateRequest;
import me.donas.boost.domain.schedule.dto.SchedulesResponse;
import me.donas.boost.domain.schedule.dto.SchedulesTotalResponse;
import me.donas.boost.domain.schedule.exception.CreatorInfoException;
import me.donas.boost.domain.schedule.exception.ScheduleException;
import me.donas.boost.domain.schedule.repository.CreatorInfoRepository;
import me.donas.boost.domain.schedule.repository.ScheduleQueryRepository;
import me.donas.boost.domain.schedule.repository.ScheduleRepository;
import me.donas.boost.global.utils.S3UploadUtils;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	private final ScheduleQueryRepository scheduleQueryRepository;
	private final CreatorInfoRepository creatorInfoRepository;
	private final S3UploadUtils s3UploadUtils;

	@Transactional
	public Long createSchedule(ScheduleRequest request, MultipartFile file) {
		CreatorInfo creatorInfo = creatorInfoRepository.findById(request.creatorId())
			.orElseThrow(() -> new CreatorInfoException(NOT_FOUND_CREATOR_INFO));
		String bannerImage = (file == null) ? "" : s3UploadUtils.upload(request.creatorId() + "/" + "banner", file);
		Schedule schedule = scheduleRepository.save(request.toEntity(creatorInfo, bannerImage));
		return schedule.getId();
	}

	@Transactional(readOnly = true)
	public SchedulesResponse readSchedulesFromAdmin(int page, int size) {
		List<ScheduleResponse> schedules = scheduleRepository
			.findAll(PageRequest.of(page, size))
			.stream()
			.map(ScheduleResponse::of)
			.toList();
		long totalCount = scheduleRepository.count();
		long totalPage = (totalCount % size == 0) ? totalCount / size : (totalCount / size) + 1;
		return SchedulesResponse.of(totalPage, schedules);
	}

	@Transactional(readOnly = true)
	public SchedulesTotalResponse readSchedules(ZonedDateTime time, int day, PlatformProvider provider, int page,
		int size) {
		SearchBetweenTime searchBetweenTime = convertTime(time);

		List<ScheduleQueryResponse> schedules = scheduleQueryRepository
			.findAllByScheduledTimeBetweenAndPlatformProvider(day, searchBetweenTime, provider);

		if (PlatformProvider.TOTAL.equals(provider)) {
			return organizeByTotal(page, size, time.withZoneSameInstant(ZoneOffset.UTC), schedules);
		} else {
			return organizeByPlatform(provider, schedules);
		}
	}

	private SchedulesTotalResponse organizeByTotal(int page, int size, ZonedDateTime now,
		List<ScheduleQueryResponse> schedules) {
		Map<PlatformProvider, List<ScheduleQueryResponse>> map = new HashMap<>();
		Map<PlatformProvider, ScheduleQueryResponses> converter = new HashMap<>();

		for (ScheduleQueryResponse r : schedules) {
			PlatformProvider p = r.getProvider();
			List<ScheduleQueryResponse> l = map.getOrDefault(p, new ArrayList<>());
			l.add(r);
			map.put(p, l);
		}

		for (Map.Entry<PlatformProvider, List<ScheduleQueryResponse>> entry : map.entrySet()) {
			List<ScheduleQueryResponse> l = entry.getValue();
			long totalPage = l.size() % size == 0 ? l.size() / size : (l.size() / size) + 1;
			long nowSize = l.stream().filter(r -> r.getScheduledTime().isBefore(now)).toList().size();
			long recommendPage = nowSize % size == 0 ? nowSize / size : (nowSize / size) + 1;
			List<ScheduleQueryResponse> s = l.stream().skip((long)page * size).limit(size).toList();
			converter.put(entry.getKey(), ScheduleQueryResponses.of(totalPage, recommendPage, s));
		}
		return SchedulesTotalResponse.of(converter);
	}

	private SchedulesTotalResponse organizeByPlatform(PlatformProvider provider,
		List<ScheduleQueryResponse> schedules) {
		Map<PlatformProvider, ScheduleQueryResponses> converter = new HashMap<>();
		converter.put(provider,
			ScheduleQueryResponses.of(1, 1, schedules.stream().filter(r -> r.getProvider().equals(provider)).toList()));
		return SchedulesTotalResponse.of(converter);
	}

	private SearchBetweenTime convertTime(ZonedDateTime now) {
		LocalDateTime date = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);
		String timeOffsetId = now.getOffset().getId();
		ZoneOffsetTransition transition = ZoneOffsetTransition.of(date, ZoneOffset.of(timeOffsetId), ZoneOffset.UTC);
		LocalDateTime start = transition.getDateTimeAfter();
		LocalDateTime end = start.plusDays(1).minusMinutes(1);
		return new SearchBetweenTime(start, end);
	}

	@Transactional
	public Long updateSchedule(ScheduleUpdateRequest request, MultipartFile file) {
		Schedule schedule = scheduleRepository.findById(request.scheduleId())
			.orElseThrow(() -> new ScheduleException(NOT_FOUND_SCHEDULE));
		CreatorInfo creatorInfo = creatorInfoRepository.findById(request.creatorId())
			.orElseThrow(() -> new CreatorInfoException(NOT_FOUND_CREATOR_INFO));
		String bannerImage = (file == null) ? schedule.getBannerImage() :
			s3UploadUtils.upload(request.creatorId() + "/" + "banner", file);
		schedule.update(creatorInfo, bannerImage, request);
		return schedule.getId();
	}

	@Transactional
	public Long deleteSchedule(Long scheduleId) {
		Schedule schedule = scheduleRepository.findById(scheduleId)
			.orElseThrow(() -> new ScheduleException(NOT_FOUND_SCHEDULE));
		schedule.remove();
		scheduleRepository.delete(schedule);
		return schedule.getId();
	}
}
