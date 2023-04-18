package me.donas.boost.domain.schedule.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.donas.boost.domain.schedule.dto.CreatorInfoUpdateRequest;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreatorInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "creator_info_id")
	Long id;

	@Column(nullable = false)
	String name;

	@Column(name = "profile_image")
	String profileImage;

	@OneToMany(mappedBy = "creatorInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Platform> platforms = new ArrayList<>();

	@Builder
	public CreatorInfo(String name, String profileImage, List<Platform> platforms) {
		this.name = name;
		this.profileImage = profileImage;
		this.platforms = platforms;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void update(CreatorInfoUpdateRequest request) {
		this.name = request.name();
	}
}
