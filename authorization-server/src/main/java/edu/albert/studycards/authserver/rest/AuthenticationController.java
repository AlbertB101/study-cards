package edu.albert.studycards.authserver.rest;

import edu.albert.studycards.authserver.domain.dto.LoginDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.LoginDto;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountPersistent;
import edu.albert.studycards.authserver.domain.persistent.JwtBlacklist;
import edu.albert.studycards.authserver.repository.JwtBlacklistRepository;
import edu.albert.studycards.authserver.repository.UserAccountRepository;
import edu.albert.studycards.authserver.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class AuthorizationController {
	
	@Autowired
	UserAccountRepository userAccRepo;
	@Qualifier("userDetailsServiceImpl")
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtBlacklistRepository jwtBlacklistRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@PostMapping(value = "/logIn")
	public ResponseEntity<?> login(@RequestBody @Valid LoginDtoImpl loginDto) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			authenticationManager.authenticate(auth);
			
			return ResponseEntity.ok(formResponseOnSuccess(loginDto/*, client*/));
		} catch (BadCredentialsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		} catch (AuthenticationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}
	
	private Map<Object, Object> formResponseOnSuccess(LoginDto loginDto) {
		String token = jwtTokenProvider.createToken(
			loginDto.getEmail(),
			Role.USER.name()/*user.getRole().name()*/);
		
		//TODO: refactor it
		UserAccountPersistent userAcc = userAccRepo.findByEmail(loginDto.getEmail()).get();
		userAcc.setToken(token);
		userAccRepo.flush();
		
		
		Map<Object, Object> response = new HashMap<>();
		response.put("nickname", loginDto.getEmail());
		response.put("token", token);
		return response;
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
		SecurityContext context = SecurityContextHolder.getContext();
		String token = jwtTokenProvider.resolveToken(request);
		
		securityContextLogoutHandler.logout(request, response, context.getAuthentication());
		securityContextLogoutHandler.setClearAuthentication(true);
		jwtBlacklistRepository.saveAndFlush(new JwtBlacklist(token));
		
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
}
