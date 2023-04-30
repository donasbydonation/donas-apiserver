package me.donas.boost.domain.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donas.boost.domain.user.application.ProfileService;
import me.donas.boost.domain.user.domain.UserPrincipal;
import me.donas.boost.domain.user.dto.ProfileUpdateRequest;
import me.donas.boost.domain.user.dto.ProfileResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProfileController {
	private final ProfileService profileService;

	@GetMapping("/profile/{username}")
	public ResponseEntity<ProfileResponse> getProfile(@PathVariable String username) {
		ProfileResponse response = profileService.getProfile(username);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/profile")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ProfileResponse> updateProfile(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@Valid @RequestPart(value = "profileData") ProfileUpdateRequest request,
		@RequestPart(value = "image", required = false) MultipartFile file
	) {
		ProfileResponse response = profileService.updateProfile(userPrincipal, request, file);
		return ResponseEntity.ok(response);
	}
}
