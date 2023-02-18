package me.donas.boost.domain.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.application.UserService;
import me.donas.boost.domain.user.domain.UserPrincipal;
import me.donas.boost.domain.user.dto.UpdatePasswordRequest;
import me.donas.boost.domain.user.dto.UpdateUserInfoRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserService userService;

	@PutMapping("/{username}/password")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Void> updatePassword(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable String username,
		@RequestBody @Valid UpdatePasswordRequest request
	) {
		userService.updatePassword(userPrincipal, username, request);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{username}/information")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Void> updateUserInformation(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable String username,
		@RequestBody UpdateUserInfoRequest request
	) {
		userService.updateUserInformation(userPrincipal, username, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{username}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Void> withdraw(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable String username
	) {
		userService.withdraw(userPrincipal, username);
		return ResponseEntity.ok().build();
	}
}
