package me.donas.boost.domain.user.application;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.domain.User;
import me.donas.boost.domain.user.domain.UserPrincipal;
import me.donas.boost.domain.user.dto.LoginRequest;
import me.donas.boost.domain.user.dto.LogoutRequest;
import me.donas.boost.domain.user.dto.RefreshRequest;
import me.donas.boost.domain.user.dto.TokenResponse;
import me.donas.boost.domain.user.dto.SignupRequest;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.domain.user.repository.UserRepository;
import me.donas.boost.global.security.JwtService;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public Long register(SignupRequest request) {
		if (isDuplicateNickname(request.nickname())) {
			throw new UserException(DUPLICATE_NICKNAME);
		}
		if (isDuplicateUsername(request.username())) {
			throw new UserException(DUPLICATE_USERNAME);
		}
		User user = userRepository.save(request.toEntity(passwordEncoder));
		return user.getId();
	}

	public TokenResponse login(LoginRequest request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.username(), request.password())
		);

		User user = userRepository.findByUsername(request.username()).orElseThrow(
			() -> new UserException(NOTFOUND)
		);

		String accessToken = jwtService.generateToken(UserPrincipal.create(user));
		String refreshToken = user.login();

		return new TokenResponse(accessToken, refreshToken);
	}

	public TokenResponse refresh(RefreshRequest request) {
		String username = request.username();
		String refreshToken = request.refreshToken();

		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new UserException(NOTFOUND)
		);
		if (!user.getRefreshToken().isValid(refreshToken)) {
			throw new UserException(EXPIRED_REFRESH_TOKEN);
		}
		String accessToken = jwtService.generateToken(UserPrincipal.create(user));
		String updateRefreshToken = user.updateRefreshToken();
		return new TokenResponse(accessToken, updateRefreshToken);
	}

	public void logout(LogoutRequest request) {
		User user = userRepository.findByUsername(request.username()).orElseThrow(
			() -> new UserException(NOTFOUND)
		);
		user.logout();
	}

	private boolean isDuplicateNickname(String nickname) {
		return userRepository.existsByProfileNickname(nickname);
	}

	private boolean isDuplicateUsername(String username) {
		return userRepository.existsByUsername(username);
	}
}
