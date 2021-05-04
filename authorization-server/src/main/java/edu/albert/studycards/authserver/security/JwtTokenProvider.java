package edu.albert.studycards.authserver.security;

import edu.albert.studycards.authserver.exception.JwtAuthenticationException;
import edu.albert.studycards.authserver.repository.JwtBlacklistRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
	
	@Autowired
	private JwtBlacklistRepository jwtBlacklistRepository;
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration}")
	private long validityInMilliseconds;
	
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
	
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claimsJws = getClaims(token);
			Date expiration = claimsJws.getBody().getExpiration();
			return !expiration.before(new Date()) &&
					!jwtBlacklistRepository.existsByToken(token);
		} catch (JwtException | IllegalArgumentException e) {
			throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
		}
	}
	
	public String getUsername(String token) {
		return getClaims(token).getBody().getSubject();
	}
	
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader(HttpHeaders.AUTHORIZATION);
	}
	
	private Jws<Claims> getClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
	}
}