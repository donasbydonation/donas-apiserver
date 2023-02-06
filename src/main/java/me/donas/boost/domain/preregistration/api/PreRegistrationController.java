package me.donas.boost.domain.preregistration.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.preregistration.application.PreRegistrationService;
import me.donas.boost.domain.preregistration.domain.PreRegistration;
import me.donas.boost.domain.preregistration.dto.PreRegistrationDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PreRegistrationController {

	private final PreRegistrationService preRegistrationService;

	@PostMapping("/pre-registrations")
	public ResponseEntity<Void> register(@RequestBody PreRegistrationDto preRegistrationDto) {
		PreRegistration register = preRegistrationService.register(preRegistrationDto);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/pre-registrations/{id}")
			.buildAndExpand(register.getId()).toUri();

		return ResponseEntity.created(location).build();
	}
}
