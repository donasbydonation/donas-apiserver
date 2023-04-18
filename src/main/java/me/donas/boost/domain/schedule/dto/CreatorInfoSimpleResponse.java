package me.donas.boost.domain.schedule.dto;

import me.donas.boost.domain.schedule.domain.CreatorInfo;

public record CreatorInfoSimpleResponse(Long id, String name) {
	public static CreatorInfoSimpleResponse of(CreatorInfo creatorInfo) {
		return new CreatorInfoSimpleResponse(creatorInfo.getId(), creatorInfo.getName());
	}
}
