package me.donas.boost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.common.admin.AdminUser;
import me.donas.boost.domain.user.repository.UserRepository;

@SpringBootApplication
@RequiredArgsConstructor
public class BoostApplication {
	private final UserRepository userRepository;
	private final AdminUser adminUser;

	public static void main(String[] args) {
		SpringApplication.run(BoostApplication.class, args);
	}

	@PostConstruct
	@Transactional
	public void createAdminUser() {
		if (!userRepository.existsByUsername(adminUser.getUsername())) {
			userRepository.save(adminUser.toEntity());
		}
	}
}
