package me.donas.boost.domain.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.donas.boost.domain.schedule.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
