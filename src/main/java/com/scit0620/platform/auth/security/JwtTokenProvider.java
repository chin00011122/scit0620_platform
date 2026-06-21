package com.scit0620.platform.auth.security;

import com.scit0620.platform.user.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

	private final Key key;
	private final long validityMillis;

	public JwtTokenProvider(
			@Value("${jwt.secret:change-this-secret-key-for-setuyeon-platform-local-dev-1234567890}") String secret,
			@Value("${jwt.validity-millis:86400000}") long validityMillis
	) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.validityMillis = validityMillis;
	}

	public String createToken(Long userId, String email, Role role) {
		Date now = new Date();
		Date expiresAt = new Date(now.getTime() + validityMillis);

		return Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("email", email)
				.claim("role", role.name())
				.setIssuedAt(now)
				.setExpiration(expiresAt)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public UserPrincipal parseToken(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

		return new UserPrincipal(
				Long.valueOf(claims.getSubject()),
				claims.get("email", String.class),
				Role.valueOf(claims.get("role", String.class))
		);
	}
}
