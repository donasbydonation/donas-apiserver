package me.donas.boost.domain.preregistration.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.preregistration.domain.PreRegistration;
import me.donas.boost.domain.preregistration.dto.PreRegistrationDto;
import me.donas.boost.domain.preregistration.exception.DuplicateEmailException;
import me.donas.boost.domain.preregistration.repository.PreRegistrationRepository;

@Service
@RequiredArgsConstructor
public class PreRegistrationService {

	private final PreRegistrationRepository registrationRepository;

	@Transactional
	public PreRegistration register(PreRegistrationDto registrationDto) {
		if (registrationRepository.existsByEmail(registrationDto.email())) {
			throw new DuplicateEmailException("중복된 이메일 입니다.");
		}
		PreRegistration preRegistration = registrationDto.toEntity();
		return registrationRepository.save(preRegistration);
	}
}
