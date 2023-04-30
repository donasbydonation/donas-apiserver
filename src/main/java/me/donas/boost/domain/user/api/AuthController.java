package me.donas.boost.domain.user.api;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.application.AuthService;
import me.donas.boost.domain.user.dto.LoginRequest;
import me.donas.boost.domain.user.dto.LogoutRequest;
import me.donas.boost.domain.user.dto.RefreshRequest;
import me.donas.boost.domain.user.dto.TokenResponse;
import me.donas.boost.domain.user.dto.SignupRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest request) {
		Long id = authService.register(request);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/users/{id}")
			.buildAndExpand(id).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/nickname")
	public ResponseEntity<Void> validateDuplicationNickname(@RequestParam(defaultValue = "") String value) {
		if (authService.validateNickname(value)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/username")
	public ResponseEntity<Void> validateDuplicationUsername(@RequestParam(defaultValue = "") String value) {
		if (authService.validateUsername(value)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
		TokenResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestBody @Valid LogoutRequest request) {
		authService.logout(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshRequest request) {
		TokenResponse response = authService.refresh(request);
		return ResponseEntity.ok(response);
	}
}
