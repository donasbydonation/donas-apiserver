package me.donas.boost.global.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.donas.boost.domain.user.exception.UserException;
import me.donas.boost.global.dto.CustomErrorResponse;
import me.donas.boost.global.exception.ErrorCode;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		ServletException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			filterChain.doFilter(request, response);
		} catch (UserException e) {
			ErrorCode errorCode = e.errorCode();
			response.setStatus(errorCode.httpStatus().value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			objectMapper.writeValue(response.getWriter(), CustomErrorResponse.of(e.errorCode()));
		}
	}
}
