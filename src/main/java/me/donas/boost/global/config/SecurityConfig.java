package me.donas.boost.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
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
				authorizeRequest.requestMatchers("/docs/**").permitAll();
				authorizeRequest.requestMatchers("/api/v1/pre-registrations").permitAll()
					.requestMatchers("/api/v1/email-certificates").permitAll()
					.requestMatchers("/api/v1/signup").permitAll()
					.requestMatchers("/api/v1/login").permitAll()
					.requestMatchers("/api/v1/refresh").permitAll()
					.requestMatchers("/api/v1/nickname").permitAll()
					.requestMatchers("/api/v1/profile/**").permitAll()
					.requestMatchers("/api/v1/profile").permitAll()
					.requestMatchers("/api/v1/username").permitAll()
					.requestMatchers("/api/v1/admin/**").permitAll()
					.requestMatchers("/api/v1/creator-infos/**").permitAll()
					.requestMatchers("/api/v1/health").permitAll()
					.requestMatchers("/api/v1/schedules/**").permitAll()
					.requestMatchers("/api/v2/schedules/**").permitAll()
					.requestMatchers("/api/v2/creators/**").permitAll()
					.anyRequest().authenticated();
			}).authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

		return http.build();
	}
}
