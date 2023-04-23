package me.donas.boost.domain.schedule.dto;

import java.util.List;

public record CreatorInfosResponse(
	long totalPage,
	List<CreatorInfoResponse> creatorInfos
) {
	public static CreatorInfosResponse of(long totalPage, List<CreatorInfoResponse> creatorInfos) {
		return new CreatorInfosResponse(totalPage, creatorInfos);
	}
}
