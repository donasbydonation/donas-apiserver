package me.donas.boost.domain.schedule.application;

import static me.donas.boost.domain.schedule.exception.CreatorInfoErrorCode.*;
import static me.donas.boost.domain.schedule.exception.ScheduleErrorCode.*;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.Schedule;
import me.donas.boost.domain.schedule.dto.CreatorInfoRequest;
import me.donas.boost.domain.schedule.dto.ScheduleRequest;
import me.donas.boost.domain.schedule.dto.ScheduleResponse;
import me.donas.boost.domain.schedule.dto.ScheduleUpdateRequest;
import me.donas.boost.domain.schedule.exception.CreatorInfoErrorCode;
import me.donas.boost.domain.schedule.exception.CreatorInfoException;
import me.donas.boost.domain.schedule.exception.ScheduleErrorCode;
import me.donas.boost.domain.schedule.exception.ScheduleException;
import me.donas.boost.domain.schedule.repository.CreatorInfoRepository;
import me.donas.boost.domain.schedule.repository.PlatformRepository;
import me.donas.boost.domain.schedule.repository.ScheduleRepository;
import me.donas.boost.global.utils.S3UploadUtils;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
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
	public List<ScheduleResponse> readSchedulesFromAdmin(int page, int size) {
		return scheduleRepository.findAll(PageRequest.of(page, size)).stream().map(ScheduleResponse::of).toList();
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
