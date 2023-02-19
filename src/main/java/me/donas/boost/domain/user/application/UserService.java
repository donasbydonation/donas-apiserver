package me.donas.boost.domain.user.application;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.domain.User;
import me.donas.boost.domain.user.domain.UserPrincipal;
import me.donas.boost.domain.user.dto.UpdatePasswordRequest;
import me.donas.boost.domain.user.dto.UpdateUserInfoRequest;
import me.donas.boost.domain.user.dto.UserInfoResponse;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public UserInfoResponse getUserInformation(UserPrincipal userPrincipal, String username) {
		validateUsername(userPrincipal, username);
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException(NOTFOUND));
		return UserInfoResponse.of(user);
	}

	public void updatePassword(UserPrincipal userPrincipal, String username, UpdatePasswordRequest request) {
		validateUsername(userPrincipal, username);
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException(NOTFOUND));
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new UserException(INVALID_PASSWORD);
		}
		user.updatePassword(passwordEncoder.encode(request.updatePassword()));
	}

	public void updateUserInformation(UserPrincipal userPrincipal, String username, UpdateUserInfoRequest request) {
		validateUsername(userPrincipal, username);
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException(NOTFOUND));
		user.updateUserInformation(request);
	}

	public void withdraw(UserPrincipal userPrincipal, String username) {
		validateUsername(userPrincipal, username);
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException(NOTFOUND));
		user.withdraw();
	}

	private void validateUsername(UserPrincipal userPrincipal, String username) {
		if (!userPrincipal.getUsername().equals(username)) {
			throw new UserException(NOTFOUND);
		}
	}
}
