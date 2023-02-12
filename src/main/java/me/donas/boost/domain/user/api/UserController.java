package me.donas.boost.domain.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.application.UserService;
import me.donas.boost.domain.user.dto.UpdatePasswordRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserService userService;

	@PutMapping("/{id}/password")
	public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody @Valid UpdatePasswordRequest request) {
		userService.updatePassword(id, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> withdraw(@PathVariable Long id) {
		userService.withdraw(id);
		return ResponseEntity.ok().build();
	}
}
