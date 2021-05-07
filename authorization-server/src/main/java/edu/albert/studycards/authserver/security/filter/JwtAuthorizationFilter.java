package edu.albert.studycards.authserver.security.filter;

import edu.albert.studycards.authserver.exception.JwtAuthenticationException;
import edu.albert.studycards.authserver.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	public JwtAuthorizationFilter() {
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = jwtTokenProvider.resolveToken(request);
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication auth = jwtTokenProvider.getAuthentication(token);
				if (auth != null)
					SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (JwtAuthenticationException e) {
			SecurityContextHolder.clearContext();
			response.sendError(e.getHttpStatus().value());
			throw new JwtAuthenticationException(e.getMessage());
		} catch (BadCredentialsException e) {
			response.sendError(HttpStatus.UNAUTHORIZED.value());
			throw new BadCredentialsException(e.getMessage());
		}
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return requestURI.equals("/api/v1/account/signUp") ||
			       requestURI.equals("/api/v1/auth/logIn");
	}
}
