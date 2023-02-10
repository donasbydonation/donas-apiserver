package me.donas.boost.domain.preregistration.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.preregistration.application.PreRegistrationService;
import me.donas.boost.domain.preregistration.dto.PreRegistrationRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PreRegistrationController {

	private final PreRegistrationService preRegistrationService;

	@PostMapping("/pre-registrations")
	public ResponseEntity<Void> register(@RequestBody @Valid PreRegistrationRequest request) {
		Long registerId = preRegistrationService.register(request);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/pre-registrations/{id}")
			.buildAndExpand(registerId).toUri();

		return ResponseEntity.created(location).build();
	}
}
