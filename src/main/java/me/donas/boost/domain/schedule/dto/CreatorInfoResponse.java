package me.donas.boost.domain.schedule.dto;

import java.util.Comparator;
import java.util.List;

import me.donas.boost.domain.schedule.domain.CreatorInfo;

public record CreatorInfoResponse(
	Long creatorInfoId,
	String name,
	String profileImage,
	List<PlatformResponse> platformResponses
) {
	public static CreatorInfoResponse of(CreatorInfo creatorInfo) {
		return new CreatorInfoResponse(creatorInfo.getId(), creatorInfo.getName(), creatorInfo.getProfileImage(),
			creatorInfo.getPlatforms().stream()
				.map(PlatformResponse::of)
				.sorted(Comparator.comparing(PlatformResponse::provider))
				.toList());
	}
}
