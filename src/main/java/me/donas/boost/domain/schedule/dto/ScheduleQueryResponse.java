package me.donas.boost.domain.schedule.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donas.boost.domain.schedule.domain.PlatformProvider;

@Getter
@NoArgsConstructor
public class ScheduleQueryResponse {
	private Long creatorId;
	private String creatorName;
	private String profileImage;
	private PlatformProvider provider;
	private String broadcastLink;
	private Long scheduleId;
	private String title;
	private String bannerImage;
	private String description;
	private ZonedDateTime scheduledTime;

	@QueryProjection
	public ScheduleQueryResponse(Long creatorId, String creatorName, String profileImage, PlatformProvider provider,
		String broadcastLink, Long scheduleId, String title, String bannerImage, String description, LocalDateTime scheduledTime) {
		this.creatorId = creatorId;
		this.creatorName = creatorName;
		this.profileImage = profileImage;
		this.provider = provider;
		this.broadcastLink = broadcastLink;
		this.scheduleId = scheduleId;
		this.title = title;
		this.bannerImage = bannerImage;
		this.description = description;
		this.scheduledTime = scheduledTime.atZone(ZoneId.of("UTC"));
	}
}
