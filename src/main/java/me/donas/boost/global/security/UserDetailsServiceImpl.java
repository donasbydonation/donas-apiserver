package me.donas.boost.global.security;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.user.domain.User;
import me.donas.boost.domain.user.domain.UserPrincipal;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException(NOTFOUND));
		return UserPrincipal.create(user);
	}
}
