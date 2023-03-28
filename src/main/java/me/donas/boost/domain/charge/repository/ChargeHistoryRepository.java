package me.donas.boost.domain.charge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.donas.boost.domain.charge.domain.ChargeHistory;

public interface ChargeHistoryRepository extends JpaRepository<ChargeHistory, Long> {
}
