package edu.albert.studycards.resourceserver.security;


import edu.albert.studycards.resourceserver.exceptions.JwtAuthenticationException;
import edu.albert.studycards.resourceserver.model.interfaces.Role;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration}")
	private long validityInMilliseconds;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String createToken(String username, String role) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("role", role);
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds * 1000);
		
		return Jwts.builder()
			       .setClaims(claims)
			       .setIssuedAt(now)
			       .setExpiration(validity)
			       .signWith(SignatureAlgorithm.HS256, secretKey)
			       .compact();
	}
	
	public String resolveToken(HttpServletRequest request) throws BadCredentialsException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || header.isEmpty()) {
			throw new BadCredentialsException("Http authorization header doesn't exist");
		}
		return header;
	}
	
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claimsJws = getClaims(token);
			Date expiration = claimsJws.getBody().getExpiration();
			return !expiration.before(new Date());
//					&& !jwtBlacklistRepository.existsByToken(token);
		} catch (JwtException | IllegalArgumentException e) {
			throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
		}
	}
	
	public Authentication getAuthentication(String token) {
		Jws<Claims> claims = getClaims(token);
		String role = claims.getBody().get("role", String.class);
		try {
			return new UsernamePasswordAuthenticationToken(
				claims.getBody().getSubject(),
				null,
				Role.valueOf(role).getAuthorities());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Jws<Claims> getClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
	}
}