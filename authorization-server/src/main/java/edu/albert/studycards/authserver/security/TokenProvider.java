package edu.albert.studycards.authserver.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface TokenProvider {
	String createToken(String username, String role);
	
	String resolveToken(HttpServletRequest request) throws BadCredentialsException;
	
	boolean validateToken(String token);
	
	Authentication getAuthentication(String token);
	
}
