package me.donas.boost.domain.schedule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donas.boost.domain.schedule.dto.PlatformRequest;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Platform {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "platform_id")
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_info_id")
	CreatorInfo creatorInfo;

	@Enumerated(EnumType.STRING)
	PlatformProvider provider;

	String broadcastLink;

	@Builder
	public Platform(CreatorInfo creatorInfo, PlatformProvider provider, String broadcastLink) {
		setCreatorInfo(creatorInfo);
		this.provider = provider;
		this.broadcastLink = broadcastLink;
	}

	private void setCreatorInfo(CreatorInfo creatorInfo) {
		creatorInfo.getPlatforms().add(this);
		this.creatorInfo = creatorInfo;
	}

	public void update(PlatformRequest request) {
		this.broadcastLink = request.broadcastLink();
	}

	public boolean validateCreator(Long creatorInfoId) {
		return this.creatorInfo.getId().equals(creatorInfoId);
	}

	public void remove() {
		this.creatorInfo = null;
	}
}
