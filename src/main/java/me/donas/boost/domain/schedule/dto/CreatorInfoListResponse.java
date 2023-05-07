package me.donas.boost.domain.schedule.dto;

import java.util.Comparator;
import java.util.List;

import me.donas.boost.domain.schedule.domain.CreatorInfo;

public record CreatorInfoListResponse(
	Long creatorId,
	String name,
	String profileImage,
	List<PlatformResponse> platforms
) {
	public static CreatorInfoListResponse of(CreatorInfo creatorInfo) {
		return new CreatorInfoListResponse(
			creatorInfo.getId(),
			creatorInfo.getName(),
			creatorInfo.getProfileImage(),
			creatorInfo.getPlatforms()
				.stream()
				.map(PlatformResponse::of)
				.sorted(Comparator.comparing(PlatformResponse::provider))
				.toList()
		);
	}
}
