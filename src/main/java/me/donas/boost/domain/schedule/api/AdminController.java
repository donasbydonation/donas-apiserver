package me.donas.boost.domain.schedule.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.schedule.application.AdminService;
import me.donas.boost.domain.user.dto.LoginRequest;
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
}
