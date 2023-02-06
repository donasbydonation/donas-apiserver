package me.donas.boost.domain.preregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.donas.boost.domain.preregistration.domain.PreRegistration;

public interface PreRegistrationRepository extends JpaRepository<PreRegistration, Long> {

	boolean existsByEmail(String email);
}
