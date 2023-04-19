package me.donas.boost.domain.common.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.common.application.AdminService;
import me.donas.boost.domain.user.dto.LoginRequest;
import me.donas.boost.domain.user.dto.RefreshRequest;
import me.donas.boost.domain.user.dto.TokenResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {
	private final AdminService adminService;

	@PostMapping("/admin/login")
	public ResponseEntity<TokenResponse> adminLogin(@RequestBody @Valid LoginRequest request) {
		TokenResponse tokenResponse = adminService.adminLogin(request);
		return ResponseEntity.ok(tokenResponse);
	}

	@PostMapping("/admin/refresh")
	public ResponseEntity<TokenResponse> adminRefresh(@RequestBody @Valid RefreshRequest request) {
		TokenResponse tokenResponse = adminService.adminRefresh(request);
		return ResponseEntity.ok(tokenResponse);
	}
}
