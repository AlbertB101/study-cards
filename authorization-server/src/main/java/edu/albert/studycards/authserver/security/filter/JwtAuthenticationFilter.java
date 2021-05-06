package edu.albert.studycards.authserver.security.filter;

import edu.albert.studycards.authserver.domain.interfaces.UserAccountPersistent;
import edu.albert.studycards.authserver.repository.UserAccountRepository;
import edu.albert.studycards.authserver.exception.JwtAuthenticationException;
import edu.albert.studycards.authserver.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserAccountRepository userAccRepo;
    
    public JwtAuthenticationFilter() {
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new JwtAuthenticationException("JWT token is expired or invalid");
            }
            
            String username = jwtTokenProvider.getUsername(token);
            UserAccountPersistent account = userAccRepo.findByEmail(username)
                                         .orElseThrow(() -> new BadCredentialsException("Such user doesn't exist"));
            
            var authRequest = new UsernamePasswordAuthenticationToken(
                username,
                null,
	            account.getRole().getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        filterChain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.equals("/api/v1/auth/signUp") ||
                   requestURI.equals("/api/v1/auth/login");
    }
}
