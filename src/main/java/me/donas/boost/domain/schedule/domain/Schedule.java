package me.donas.boost.domain.schedule.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donas.boost.domain.schedule.dto.ScheduleUpdateRequest;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_info_id")
	CreatorInfo creatorInfo;

	String title;

	String bannerImage;

	@Lob
	String description;

	LocalDateTime scheduledTime;

	@Builder
	public Schedule(CreatorInfo creatorInfo, String title, String bannerImage, String description,
		LocalDateTime scheduledTime) {
		this.creatorInfo = creatorInfo;
		this.title = title;
		this.bannerImage = bannerImage;
		this.description = description;
		this.scheduledTime = scheduledTime;
	}

	public void update(CreatorInfo creatorInfo, String bannerImage, ScheduleUpdateRequest request) {
		this.creatorInfo = creatorInfo;
		this.bannerImage = bannerImage;
		this.title = request.title();
		this.description = request.description();
		this.scheduledTime = request.scheduledTime();
	}

	public void remove() {
		this.creatorInfo = null;
	}
}
