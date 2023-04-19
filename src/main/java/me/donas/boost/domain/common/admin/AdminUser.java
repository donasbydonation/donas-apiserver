package me.donas.boost.domain.common.admin;

import org.springframework.boot.context.properties.ConfigurationProperties;

import me.donas.boost.domain.user.domain.Role;
import me.donas.boost.domain.user.domain.User;

@ConfigurationProperties(prefix = "admin")
public class AdminUser {
	String username;
	String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User toEntity() {
		return User.builder()
			.username(username)
			.password(password)
			.role(Role.ROLE_ADMIN)
			.build();
	}
}
