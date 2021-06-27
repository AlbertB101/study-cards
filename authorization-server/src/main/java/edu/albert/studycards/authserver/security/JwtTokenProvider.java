package edu.albert.studycards.authserver.security;

import edu.albert.studycards.authserver.exception.JwtAuthenticationException;
import edu.albert.studycards.authserver.repository.JwtBlacklistRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenProvider {
	
	@Autowired
	private JwtBlacklistRepository jwtBlacklistRepository;
	@Qualifier("userDetailsServiceImpl")
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration}")
	private long validityInMilliseconds;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	@Override
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
	
	@Override
	public String resolveToken(HttpServletRequest request) throws BadCredentialsException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || header.isEmpty()) {
			throw new BadCredentialsException("Http authorization header doesn't exist");
		}
		return header;
	}
	
	@Override
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
	
	@Override
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(
			userDetails.getUsername(),
			null,
			userDetails.getAuthorities());
	}
	
	private String getUsername(String token) {
		return getClaims(token).getBody().getSubject();
	}
	
	private Jws<Claims> getClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
	}
}