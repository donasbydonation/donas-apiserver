package me.donas.boost.domain.user.api;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
import me.donas.boost.domain.user.dto.ProfileUpdateResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProfileController {
	private final ProfileService profileService;

	@PutMapping("/profile")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ProfileUpdateResponse> updateProfile(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@Valid @RequestPart(value = "profileData") ProfileUpdateRequest request,
		@RequestPart(value = "image", required = false) MultipartFile file
	) {
		ProfileUpdateResponse response = profileService.updateProfile(userPrincipal, request, file);
		return ResponseEntity.ok(response);
	}
}
