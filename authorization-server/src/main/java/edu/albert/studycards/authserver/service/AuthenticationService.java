package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.dto.LoginDto;
import edu.albert.studycards.authserver.domain.persistent.JwtBlacklist;
import edu.albert.studycards.authserver.repository.JwtBlacklistRepository;
import edu.albert.studycards.authserver.repository.UserAccountRepository;
import edu.albert.studycards.authserver.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service class helps to satisfy login and logout requests
 */
@Service
public class AuthenticationService {
	
	@Autowired
	private UserAccountRepository userAccRepo;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private TokenProvider jwtTokenProvider;
	@Autowired
	private JwtBlacklistRepository jwtBlacklistRepo;
	
	/**
	 * This method logs registered user in.
	 *
	 * <p>If login was successful, method returns {@link Map} that contains such key-value pairs:
	 * <ul>
	 * 	<li>email - email of logged user;
	 *  <li>token - jwt token for accessing resources those require authorization.
	 * </ul>
	 * Valid token gives access to resource-server that contains domain model endpoints
	 * (managing LangPacks and Cards) and to authorization server (managing user account).
	 *
	 * <p> If attempt to log in is failed, AuthenticationException is thrown.
	 *
	 * @param loginDto must have email and password
	 * @return {@link Map} that contains info for request response
	 * @throws AuthenticationException if login attempt is failed
	 */
	public CompletableFuture<Map<String, Object>> login(LoginDto loginDto) {
		var authToken = new UsernamePasswordAuthenticationToken(
			loginDto.getEmail(),
			loginDto.getPassword());
		authManager.authenticate(authToken);
		var userAcc = userAccRepo.findByEmail(loginDto.getEmail())
			              .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
		String token = jwtTokenProvider.createToken(userAcc.getEmail(), userAcc.getRole().name());
		
		//TODO: Add event of new user account creation
		userAcc.setToken(token);
		
		return CompletableFuture.completedFuture(formResponseOnSuccess(userAcc.getEmail(), token));
	}
	
	/**
	 * This method helps form a response for controller
	 * @param email
	 * @param token
	 * @return
	 */
	private Map<String, Object> formResponseOnSuccess(String email, String token) {
		Map<String, Object> response = new HashMap<>();
		response.put("email", email);
		response.put("token", token);
		return response;
	}
	
	/**
	 * This method processes logout request.
	 *
	 * <p>This method needed further work
	 *
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return CompletableFuture with void value
	 * @throws AuthenticationException if logout attempt is failed
	 */
	public CompletableFuture<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		SecurityContext context = SecurityContextHolder.getContext();
		String token = jwtTokenProvider.resolveToken(request);
		logoutHandler.logout(request, response, context.getAuthentication());
		logoutHandler.setClearAuthentication(true);
		jwtBlacklistRepo.saveAndFlush(new JwtBlacklist(token));
		return CompletableFuture.completedFuture(null);
	}
}