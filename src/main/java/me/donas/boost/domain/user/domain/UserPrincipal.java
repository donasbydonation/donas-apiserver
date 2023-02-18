package me.donas.boost.domain.user.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;

public class UserPrincipal implements UserDetails {
	private final Long id;
	private final String username;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;
	private final boolean accountExpired;

	@Builder
	protected UserPrincipal(Long id, String username, String password, boolean accountExpired,
		Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.accountExpired = accountExpired;
		this.authorities = authorities;
	}

	public static UserPrincipal create(User user) {
		return UserPrincipal.builder()
			.id(user.getId())
			.username(user.getUsername())
			.password(user.getPassword())
			.accountExpired(user.isWithdraw())
			.authorities(List.of(new SimpleGrantedAuthority(user.getRole().name()))).build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public Long getId() {
		return this.id;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
