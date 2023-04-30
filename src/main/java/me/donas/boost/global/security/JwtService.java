package me.donas.boost.global.security;

import static me.donas.boost.domain.user.exception.UserErrorCode.*;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import me.donas.boost.domain.common.admin.AdminUser;
import me.donas.boost.domain.user.exception.UserException;

@Slf4j
@Service
public class JwtService {
	private final long tokenExpirationMs;
	private final long adminTokenExpirationMs;
	private final Key key;

	public JwtService(
		@Value("${jwt.secret-key}") String secretKey,
		@Value("${jwt.expiration-ms}") long tokenExpirationMs,
		@Value("${jwt.admin-expiration-ms}") long adminTokenExpirationMs
	) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.tokenExpirationMs = tokenExpirationMs;
		this.adminTokenExpirationMs = adminTokenExpirationMs;
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateAdminToken(AdminUser admin) {
		long currentTime = System.currentTimeMillis();
		return Jwts.builder()
			.setClaims(new HashMap<>())
			.setSubject(admin.getUsername())
			.setIssuedAt(new Date(currentTime))
			.setExpiration(new Date(currentTime + adminTokenExpirationMs))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		long currentTime = System.currentTimeMillis();
		return Jwts.builder()
			.setClaims(extraClaims)
			.setSubject(userDetails.getUsername())
			.setIssuedAt(new Date(currentTime))
			.setExpiration(new Date(currentTime + tokenExpirationMs))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			throw new UserException(INVALID_JWT_SIGNATURE);
		} catch (MalformedJwtException e) {
			throw new UserException(INVALID_JWT_TOKEN);
		} catch (ExpiredJwtException e) {
			throw new UserException(EXPIRED_JWT_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new UserException(UNSUPPORTED_JWT_TOKEN);
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}

	private Claims extractAllClaims(String token) {
		return Jwts
			.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

}
