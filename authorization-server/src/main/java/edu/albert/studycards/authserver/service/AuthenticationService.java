package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.interfaces.LoginDto;
import edu.albert.studycards.authserver.domain.persistent.JwtBlacklist;
import edu.albert.studycards.authserver.repository.JwtBlacklistRepository;
import edu.albert.studycards.authserver.repository.UserAccountRepository;
import edu.albert.studycards.authserver.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthenticationService {
	
	@Autowired
	UserAccountRepository userAccRepo;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private JwtBlacklistRepository jwtBlacklistRepository;
	
	public CompletableFuture<Map<String, Object>> login(LoginDto loginDto) throws BadCredentialsException {
		try {
			var authToken = new UsernamePasswordAuthenticationToken(
				loginDto.getEmail(),
				loginDto.getPassword());
			authManager.authenticate(authToken);
			var userAcc = userAccRepo.findByEmail(loginDto.getEmail())
				              .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
			String token = jwtTokenProvider.createToken(userAcc.getEmail(), userAcc.getRole().name());
			//TODO: Add event of new user account creation
			userAcc.setToken(token);
			return CompletableFuture.completedFuture(formResponseOnSuccess(userAcc.getEmail(), token));
		} catch (UsernameNotFoundException | BadCredentialsException e) {
			throw new BadCredentialsException(e.getMessage());
		}
	}
	
	private Map<String, Object> formResponseOnSuccess(String email, String token) {
		Map<String, Object> response = new HashMap<>();
		response.put("email", email);
		response.put("token", token);
		return response;
	}
	
	public CompletableFuture<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		//TODO: figure out exceptions throwing
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		SecurityContext context = SecurityContextHolder.getContext();
		String token = jwtTokenProvider.resolveToken(request);
		logoutHandler.logout(request, response, context.getAuthentication());
		logoutHandler.setClearAuthentication(true);
		jwtBlacklistRepository.saveAndFlush(new JwtBlacklist(token));
		return CompletableFuture.completedFuture(null);
	}
}