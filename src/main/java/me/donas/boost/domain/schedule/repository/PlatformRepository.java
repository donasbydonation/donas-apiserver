package me.donas.boost.domain.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.Platform;
import me.donas.boost.domain.schedule.domain.PlatformProvider;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
	List<Platform> findAllByCreatorInfoId(Long creatorInfoId);
	boolean existsByCreatorInfoAndProvider(CreatorInfo creatorInfo, PlatformProvider provider);
}
