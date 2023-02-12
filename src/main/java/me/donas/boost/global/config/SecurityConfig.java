package me.donas.boost.global.config;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.donas.boost.global.security.JwtAuthenticationFilter;
import me.donas.boost.global.security.JwtExceptionFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtExceptionFilter jwtExceptionFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors().and()
			.formLogin().disable()
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests(authorizeRequest -> {
				authorizeRequest.requestMatchers("/api/v1/pre-registrations").permitAll()
					.requestMatchers("/api/v1/confirm-email-certification").permitAll()
					.requestMatchers("/api/v1/issue-email-certification").permitAll()
					.requestMatchers("/api/v1/signup").permitAll()
					.requestMatchers("/api/v1/login").permitAll()
					.requestMatchers("/api/v1/refresh").permitAll()
					.anyRequest().authenticated();
			}).authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

		return http.build();
	}
}
