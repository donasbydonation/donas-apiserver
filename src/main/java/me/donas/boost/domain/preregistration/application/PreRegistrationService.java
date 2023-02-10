package me.donas.boost.domain.preregistration.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.preregistration.domain.PreRegistration;
import me.donas.boost.domain.preregistration.dto.PreRegistrationRequest;
import me.donas.boost.domain.preregistration.exception.DuplicateEmailException;
import me.donas.boost.domain.preregistration.repository.PreRegistrationRepository;
import me.donas.boost.global.exception.CommonErrorCode;

@Service
@RequiredArgsConstructor
public class PreRegistrationService {

	private final PreRegistrationRepository registrationRepository;

	@Transactional
	public Long register(PreRegistrationRequest request) {
		if (registrationRepository.existsByEmail(request.email())) {
			throw new DuplicateEmailException(CommonErrorCode.DUPLICATE_EMAIL);
		}
		PreRegistration preRegistration = registrationRepository.save(request.toEntity());
		return preRegistration.getId();
	}
}
