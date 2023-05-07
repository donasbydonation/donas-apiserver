package me.donas.boost.domain.schedule.application;

import static me.donas.boost.domain.schedule.exception.CreatorInfoErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.Platform;
import me.donas.boost.domain.schedule.domain.PlatformProvider;

import me.donas.boost.domain.schedule.dto.CreatorInfoListResponse;
import me.donas.boost.domain.schedule.dto.CreatorInfoRequest;
import me.donas.boost.domain.schedule.dto.CreatorInfoResponse;
import me.donas.boost.domain.schedule.dto.CreatorInfoSimpleResponse;
import me.donas.boost.domain.schedule.dto.CreatorInfoUpdateRequest;
import me.donas.boost.domain.schedule.dto.CreatorInfosResponse;
import me.donas.boost.domain.schedule.dto.PlatformRequest;
import me.donas.boost.domain.schedule.exception.CreatorInfoException;
import me.donas.boost.domain.schedule.repository.CreatorInfoRepository;
import me.donas.boost.domain.schedule.repository.PlatformRepository;
import me.donas.boost.global.utils.S3UploadUtils;

@Service
@RequiredArgsConstructor
public class CreatorInfoService {
	private final PlatformRepository platformRepository;
	private final CreatorInfoRepository creatorInfoRepository;
	private final S3UploadUtils s3UploadUtils;

	@Transactional(readOnly = true)
	public CreatorInfoResponse readCreatorInfo(Long id) {
		CreatorInfo creatorInfo = creatorInfoRepository.findById(id).orElseThrow(() ->
			new CreatorInfoException(NOT_FOUND_CREATOR_INFO)
		);
		return CreatorInfoResponse.of(creatorInfo);
	}

	@Transactional(readOnly = true)
	public CreatorInfosResponse readCreatorInfosFromAdmin(int size, int page) {
		List<CreatorInfoResponse> creatorInfos = creatorInfoRepository
			.findAll(PageRequest.of(page, size))
			.stream()
			.map(CreatorInfoResponse::of)
			.toList();

		long totalCount = creatorInfoRepository.count();
		long totalPage = (totalCount % size == 0) ? totalCount / size : (totalCount / size) + 1;
		return CreatorInfosResponse.of(totalPage, creatorInfos);
	}

	@Transactional(readOnly = true)
	public List<CreatorInfoSimpleResponse> readCreatorsNameWithId() {
		return creatorInfoRepository.findAll().stream().map(CreatorInfoSimpleResponse::of).toList();
	}

	@Transactional
	public Long createCreatorInfo(CreatorInfoRequest request, MultipartFile multipartFile) {
		if (creatorInfoRepository.existsByName(request.name())) {
			throw new CreatorInfoException(DUPLICATE_CREATOR_NAME);
		}
		String profileImage =
			(multipartFile == null) ? "" : s3UploadUtils.upload("creator-infos/" + UUID.randomUUID(), multipartFile);
		CreatorInfo creatorInfo = creatorInfoRepository.save(request.toEntity(profileImage));
		List<Platform> platforms = request.platforms().stream().map(r -> r.toEntity(creatorInfo)).toList();
		platformRepository.saveAll(platforms);

		return creatorInfo.getId();
	}

	@Transactional
	public Long updateCreatorInfo(CreatorInfoUpdateRequest request, MultipartFile multipartFile) {
		CreatorInfo creatorInfo = creatorInfoRepository.findById(request.id())
			.orElseThrow(() -> new CreatorInfoException(NOT_FOUND_CREATOR_INFO));

		String profileImage = (multipartFile == null) ? creatorInfo.getProfileImage() :
			s3UploadUtils.upload("creator-infos/" + UUID.randomUUID(), multipartFile);

		creatorInfo.updateProfileImage(profileImage);
		creatorInfo.update(request);

		Map<PlatformProvider, PlatformRequest> platformUpdateRequests = new HashMap<>();
		Map<PlatformProvider, Platform> platformInfos = new HashMap<>();

		for (PlatformRequest p : request.platforms()) {
			platformUpdateRequests.put(PlatformProvider.valueOf(p.platform().toUpperCase()), p);
		}
		for (Platform p : creatorInfo.getPlatforms()) {
			platformInfos.put(p.getProvider(), p);
		}
		for (PlatformProvider provider : PlatformProvider.values()) {
			PlatformRequest updateReq = platformUpdateRequests.getOrDefault(provider, null);
			Platform platform = platformInfos.getOrDefault(provider, null);
			if (updateReq != null && platform != null) {
				platform.update(updateReq);
			} else if (updateReq == null && platform != null) {
				platform.remove();
				platformRepository.delete(platform);
			} else if (updateReq != null && platform == null) {
				platformRepository.save(updateReq.toEntity(creatorInfo));
			}
		}
		return creatorInfo.getId();
	}

	@Transactional
	public Long deleteCreatorInfo(Long id) {
		CreatorInfo creatorInfo = creatorInfoRepository.findById(id).orElseThrow(() ->
			new CreatorInfoException(NOT_FOUND_CREATOR_INFO)
		);
		creatorInfoRepository.delete(creatorInfo);
		return creatorInfo.getId();
	}

	@Transactional(readOnly = true)
	public List<CreatorInfoListResponse> searchCreatorInfo(String q) {
		return creatorInfoRepository.findAllByNameContainingIgnoreCase(q)
			.stream()
			.map(CreatorInfoListResponse::of)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<CreatorInfoListResponse> readCreatorForList() {
		return creatorInfoRepository.findAll()
			.stream()
			.map(CreatorInfoListResponse::of)
			.toList();
	}
}
