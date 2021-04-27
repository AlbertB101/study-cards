package edu.albert.studycards.authserver.rest;

import edu.albert.studycards.authserver.domain.dto.ClientDtoImpl;
import edu.albert.studycards.authserver.domain.dto.LoginDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.interfaces.LoginDto;
import edu.albert.studycards.authserver.repository.ClientRepository;
import edu.albert.studycards.authserver.repository.JwtBlacklistRepository;
import edu.albert.studycards.authserver.domain.persistent.JwtBlacklist;
import edu.albert.studycards.authserver.security.JwtTokenProvider;
import edu.albert.studycards.authserver.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/auth")
public class AuthorizationController {
	
	@Autowired
	ClientService clientService;
	@Qualifier("userDetailsServiceImpl")
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private JwtBlacklistRepository jwtBlacklistRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@PostMapping(value = "/signup")
	public ResponseEntity<?> registerClient(@RequestBody @Valid ClientDtoImpl clientDto) {
		try {
			CompletableFuture<ClientDto> compFuture = clientService.registerClient(clientDto);
			ClientDto registeredClient = compFuture.join();
			return new ResponseEntity<>(
				Map.of("ClientMetaInfo", registeredClient,
					"ResponseMessage", "Client was successfully registered"),
				HttpStatus.OK);
		} catch (Throwable e) {
			return new ResponseEntity<>("Client wasn't registered. " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginDtoImpl loginDto) {
		try {
			var client = clientRepository.findByEmail(loginDto.getEmail())
				             .orElseThrow(() -> new BadCredentialsException("The client doesn't exist"));
			
			Authentication auth = new UsernamePasswordAuthenticationToken(
				loginDto.getEmail(),
				loginDto.getPassword(),
				client.getRole().getAuthorities());
			authenticationManager.authenticate(auth);
			
			return ResponseEntity.ok(formResponseOnSuccess(loginDto, client));
		} catch (BadCredentialsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		} catch (AuthenticationException e) {
			return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
		}
	}
	
	private Map<Object, Object> formResponseOnSuccess(LoginDto loginDto, ClientPersistent user) {
		String token = jwtTokenProvider.createToken(loginDto.getEmail(), user.getRole().name());
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
		jwtBlacklistRepository.save(new JwtBlacklist(token));
		
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
}
