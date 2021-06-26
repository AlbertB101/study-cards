package edu.albert.studycards.authserver.rest;

import edu.albert.studycards.authserver.domain.dto.LoginDtoImpl;
import edu.albert.studycards.authserver.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * Authentication REST controller that exposes endpoints for login and logout requests.
 *
 * <p>As this controller is REST it doesn't hold information about clients and receives JSON files.
 *
 */
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UserAccountController.class);
	@Autowired
	AuthenticationService authService;
	
	/**
	 * Makes a try to log client in and returns {@link ResponseEntity} with login information.
	 *
	 * <p>Response entity has properties:
	 * <ul>
	 * <li>email - email of logged user;
	 * <li>token - jwt token for accessing resources those require authorization.
	 * </ul>
	 *
	 * <p>Endpoint receives json file that is converted to {@link LoginDtoImpl} object.
	 * To successfully log in received json file must satisfy annotation constraints in
	 * {@link LoginDtoImpl}.
	 *
	 * @param loginDto {@link LoginDtoImpl} object to log in.
	 * @return ResponseEntity with information about login request.
	 */
	@PostMapping(value = "/logIn")
	public ResponseEntity<?> logIn(@RequestBody @Valid LoginDtoImpl loginDto) {
		try {
			Map<String, Object> response = authService.login(loginDto).get();
			return ResponseEntity.ok(response);
		} catch (BadCredentialsException e) {
			LOGGER.info("Credentials are incorrect; Login request wasn't satisfied");
			return new ResponseEntity<>("Login fail. Email or password is incorrect", HttpStatus.FORBIDDEN);
		} catch (CancellationException e) {
			LOGGER.debug("Future completion was unexpectedly cancelled; " + e.getMessage());
			return new ResponseEntity<>("Login fail", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExecutionException e) {
			LOGGER.debug("Future was completed exceptionally; " + e.getCause());
			return new ResponseEntity<>("Login fail", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			LOGGER.debug("Future was interrupted; " + e.getMessage());
			return new ResponseEntity<>("Login fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/logOut")
	public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response) {
		try {
			authService.logout(request, response).get();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CancellationException e) {
			LOGGER.debug("Future completion was unexpectedly cancelled; " + e.getMessage());
			return new ResponseEntity<>("Logout fail", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExecutionException e) {
			LOGGER.debug("Future was completed exceptionally; " + e.getCause());
			return new ResponseEntity<>("Logout fail", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			LOGGER.debug("Future was interrupted; " + e.getMessage());
			return new ResponseEntity<>("Logout fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
