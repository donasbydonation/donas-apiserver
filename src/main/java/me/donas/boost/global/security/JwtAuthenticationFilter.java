package me.donas.boost.global.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		String jwt = authHeader.substring(7);
		if (jwtService.isTokenValid(jwt)) {
			String username = jwtService.extractUsername(jwt);
			UserDetails userPrincipal = this.userDetailsService.loadUserByUsername(username);
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userPrincipal,
				null,
				userPrincipal.getAuthorities()
			);
			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			context.setAuthentication(authToken);
			SecurityContextHolder.setContext(context);
		}
		filterChain.doFilter(request, response);
	}

}
