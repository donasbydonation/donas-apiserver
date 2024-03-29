package me.donas.boost.domain.schedule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import me.donas.boost.domain.schedule.domain.CreatorInfo;

public interface CreatorInfoRepository extends JpaRepository<CreatorInfo, Long> {
	boolean existsByName(String name);
	Optional<CreatorInfo> findByName(String name);
	List<CreatorInfo> findAllByNameContainingIgnoreCase(String name);

	@EntityGraph(attributePaths = "platforms")
	Optional<CreatorInfo> findCreatorInfoWithPlatformsById(Long id);
}
