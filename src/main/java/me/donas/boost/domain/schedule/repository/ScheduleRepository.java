package me.donas.boost.domain.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import me.donas.boost.domain.schedule.domain.CreatorInfo;
import me.donas.boost.domain.schedule.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	List<Schedule> findAllByCreatorInfoAndScheduledTimeIsGreaterThanEqual(CreatorInfo creatorInfo, LocalDateTime scheduledTime, Sort sort);
}
