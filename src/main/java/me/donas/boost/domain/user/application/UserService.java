package me.donas.boost.domain.user.application;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.domain.User;
import me.donas.boost.domain.user.dto.UpdatePasswordRequest;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public void updatePassword(Long id, UpdatePasswordRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(NOTFOUND));
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new UserException(INVALID_PASSWORD);
		}
		user.updatePassword(passwordEncoder.encode(request.updatePassword()));
	}

	public void withdraw(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(NOTFOUND));
		user.withdraw();
	}
}
