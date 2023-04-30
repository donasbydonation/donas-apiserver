package me.donas.boost.domain.common.application;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.common.admin.AdminUser;
import me.donas.boost.domain.user.domain.User;
import me.donas.boost.domain.user.dto.LoginRequest;
import me.donas.boost.domain.user.dto.RefreshRequest;
import me.donas.boost.domain.user.dto.TokenResponse;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.domain.user.repository.UserRepository;
import me.donas.boost.global.security.JwtService;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final AdminUser adminUser;
	private final UserRepository userRepository;
	private final JwtService jwtService;

	@Transactional
	public TokenResponse adminLogin(LoginRequest request) {
		if (isValidAdmin(request)) {
			User user = userRepository.findByUsername(request.username())
				.orElseThrow(() -> new UserException(NOTFOUND));
			String accessToken = jwtService.generateAdminToken(adminUser);
			String refreshToken = user.login();
			return new TokenResponse(accessToken, refreshToken);
		}
		throw new UserException(INVALID_PASSWORD);
	}

	private boolean isValidAdmin(LoginRequest request) {
		return adminUser.getUsername().equals(request.username()) && adminUser.getPassword().equals(request.password());
	}

	@Transactional
	public TokenResponse adminRefresh(RefreshRequest request) {
		String username = request.username();
		String refreshToken = request.refreshToken();
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new UserException(NOTFOUND)
		);
		if (!user.getRefreshToken().isValid(refreshToken)) {
			throw new UserException(EXPIRED_REFRESH_TOKEN);
		}
		String accessToken = jwtService.generateAdminToken(adminUser);
		String updateRefreshToken = user.updateRefreshToken();
		return new TokenResponse(accessToken, updateRefreshToken);
	}
}
