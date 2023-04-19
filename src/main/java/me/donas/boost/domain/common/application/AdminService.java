package me.donas.boost.domain.schedule.application;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.domain.User;
import me.donas.boost.domain.user.domain.UserPrincipal;
import me.donas.boost.domain.user.dto.LoginRequest;
import me.donas.boost.domain.user.dto.TokenResponse;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.domain.user.repository.UserRepository;
import me.donas.boost.global.security.JwtService;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final UserRepository userRepository;
	private final JwtService jwtService;

	@Transactional(readOnly = true)
	public TokenResponse adminLogin(LoginRequest request) {
		User user = userRepository.findByUsername(request.username())
			.orElseThrow(() -> new UserException(NOTFOUND));

		if (user.getPassword().equals(request.password())) {
			String accessToken = jwtService.generateToken(UserPrincipal.create(user));
			String refreshToken = user.login();

			return new TokenResponse(accessToken, refreshToken);
		}
		throw new UserException(INVALID_PASSWORD);
	}
}
