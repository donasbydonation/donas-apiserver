package me.donas.boost.domain.schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.donas.boost.domain.schedule.domain.CreatorInfo;

public interface CreatorInfoRepository extends JpaRepository<CreatorInfo, Long> {
	boolean existsByName(String name);
	Optional<CreatorInfo> findByName(String name);
}
