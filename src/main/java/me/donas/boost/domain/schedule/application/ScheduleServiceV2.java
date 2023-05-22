package me.donas.boost.domain.schedule.application;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneOffsetTransition;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.domain.PlatformProvider;
import me.donas.boost.domain.schedule.dto.PlatformResultResponse;
import me.donas.boost.domain.schedule.dto.ScheduleQueryResponse;
import me.donas.boost.domain.schedule.dto.ScheduleResultResponse;
import me.donas.boost.domain.schedule.dto.ScheduleResultResponses;
import me.donas.boost.domain.schedule.repository.ScheduleQueryRepository;

@Service
@RequiredArgsConstructor
public class ScheduleServiceV2 {
	private final ScheduleQueryRepository scheduleQueryRepository;

	@Transactional(readOnly = true)
	public ScheduleResultResponses readSchedules(ZonedDateTime now, int day, PlatformProvider provider, int page,
		int size) {
		SearchBetweenTime searchBetweenTime = convertTime(now);

		List<ScheduleQueryResponse> schedules = scheduleQueryRepository
			.findAllByScheduledTimeBetweenAndPlatformProvider(day, searchBetweenTime, PlatformProvider.TOTAL);

		return extractResult(now.withZoneSameInstant(ZoneOffset.UTC), provider, page, size, schedules);
	}

	private ScheduleResultResponses extractResult(ZonedDateTime now, PlatformProvider provider, int page, int size,
		List<ScheduleQueryResponse> schedules) {
		Map<Long, ScheduleResultResponse> result = new HashMap<>();
		Set<Long> scheduleIds = findScheduleIds(schedules, provider);
		Map<String, Set<PlatformResultResponse>> platforms = extractPlatforms(schedules);
		int totalSize = scheduleIds.size();
		int beforeCount = 0;

		for (ScheduleQueryResponse schedule : schedules) {
			if (scheduleIds.contains(schedule.getScheduleId())) {
				if (result.containsKey(schedule.getScheduleId())) {
					continue;
				}
				if (schedule.getScheduledTime().isBefore(now)) {
					beforeCount++;
				}
				result.put(schedule.getScheduleId(),
					ScheduleResultResponse.of(schedule, new ArrayList<>(platforms.get(schedule.getCreatorName()))));
			}
		}
		int totalPage = (int)Math.ceil((double)totalSize / size);
		int recommendPage = (beforeCount == totalSize) ? totalPage : (int)Math.ceil((beforeCount + 1.0) / size);
		int nowPage = (page == -1) ? recommendPage : page + 1;
		if (totalPage == 0) {
			return ScheduleResultResponses.empty();
		}
		return new ScheduleResultResponses(totalPage, recommendPage, nowPage, result.values()
			.stream()
			.sorted(Comparator.comparing(ScheduleResultResponse::scheduledTime))
			.skip((page == -1) ? (recommendPage - 1L) * size : (long)page * size)
			.limit(size)
			.toList()
		);
	}

	private Set<Long> findScheduleIds(List<ScheduleQueryResponse> schedules, PlatformProvider provider) {
		HashSet<Long> result = new HashSet<>();
		for (ScheduleQueryResponse schedule : schedules) {
			if (PlatformProvider.TOTAL.equals(provider) || provider.equals(schedule.getProvider())) {
				result.add(schedule.getScheduleId());
			}
		}
		return result;
	}

	private Map<String, Set<PlatformResultResponse>> extractPlatforms(List<ScheduleQueryResponse> schedules) {
		Map<String, Set<PlatformResultResponse>> platforms = new HashMap<>();
		for (ScheduleQueryResponse res : schedules) {
			String creatorName = res.getCreatorName();
			Set<PlatformResultResponse> creatorPlatforms = platforms.getOrDefault(creatorName, new HashSet<>());
			creatorPlatforms.add(PlatformResultResponse.of(res.getProvider(), res.getBroadcastLink()));
			platforms.put(creatorName, creatorPlatforms);
		}
		return platforms;
	}

	private SearchBetweenTime convertTime(ZonedDateTime now) {
		LocalDateTime date = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);
		String timeOffsetId = now.getOffset().getId();
		ZoneOffsetTransition transition = ZoneOffsetTransition.of(date, ZoneOffset.of(timeOffsetId), ZoneOffset.UTC);
		LocalDateTime start = transition.getDateTimeAfter();
		LocalDateTime end = start.plusDays(1).minusMinutes(1);
		return new SearchBetweenTime(start, end);
	}

}
