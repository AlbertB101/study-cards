package edu.albert.studycards.authserver.rest;

import edu.albert.studycards.authserver.domain.dto.AccountRegistrationRequest;
import edu.albert.studycards.authserver.domain.dto.AccountRegistrationRequestDto;
import edu.albert.studycards.authserver.domain.dto.UserAccountDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.*;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;
import edu.albert.studycards.authserver.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing user accounts. The controller exposes endpoints
 * for CRUD operations.
 *
 * <p>Request to all endpoints except registration endpoint should be authenticated.
 * Requests may have different authorities.
 * Some endpoints are available for "users" and some only for "developers".
 *
 * <p>All endpoints return {@link ResponseEntity} with JSON file
 * that contains requested data or
 * information about why the request wasn't satisfied.
 *
 * @see Role
 * @see Permission
 */
@RestController
@RequestMapping("api/v1/user/account")
public class UserAccountController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UserAccountController.class);
	
	@Autowired
	private UserAccountService userAccService;
	
	/**
	 * Sign client up and returns {@link ResponseEntity} with http status and message.
	 * Endpoint doesn't require authentication or authorization.
	 *
	 * <p>Request to this endpoint should contain a body with parameters:
	 * <ul>
	 *  <li>firstName - name of user. Max size is 64 symbols;
	 *  <li>lastName - last name of user. Max size is 64 symbols;
	 *  <li>email - email of user that wasn't used before;
	 *  <li>password - already encrypted password.
	 * </ul>
	 *
	 * <p>If a client was registered, the endpoint returns ResponseEntity with properties:
	 * <ul>
	 *  <li>ResponseMessage - message about successful registration;
	 *  <li>UserAccountInfo - information about new user account.
	 * </ul>
	 * If client wasn't registered, the endpoint returns error message in ResponseEntity body
	 *
	 * @param regRequest registration request object
	 * @return ResponseEntity with information about registration request
	 * @see AccountRegistrationRequest
	 * @see AccountRegistrationRequestDto
	 */
	@PostMapping(value = "/signUp")
	public ResponseEntity<?> signUp(@RequestBody @Valid AccountRegistrationRequestDto regRequest) {
		try {
			UserAccountDto newUserAcc = userAccService.register(regRequest).get();
			Map<String, Object> userAccInfo = Map.of(
				"UserAccountInfo", newUserAcc,
				"ResponseMessage", "Client was successfully registered");
			return new ResponseEntity<>(userAccInfo, HttpStatus.OK);
		} catch (CancellationException e) {
			LOGGER.debug("Future completion was unexpectedly cancelled; " + e.getMessage());
			return new ResponseEntity<>("New user account wasn't created", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExecutionException e) {
			LOGGER.debug("Future was completed exceptionally; " + e.getCause());
			return new ResponseEntity<>("New user account wasn't created", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			LOGGER.debug("Future was interrupted; " + e.getMessage());
			return new ResponseEntity<>("New user account wasn't created", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ClientAlreadyExistsException e) {
			LOGGER.info("New User account wasn't created because it already exists");
			return new ResponseEntity<>("Such User account already exists", HttpStatus.OK);
		}
	}
	
	
	@GetMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<?> getAccount() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			UserAccountDto userAccInfo = userAccService.receive(username).get();
			return new ResponseEntity<>(userAccInfo, HttpStatus.OK);
		} catch (CancellationException e) {
			LOGGER.debug("Future completion was unexpectedly cancelled; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't received", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExecutionException e) {
			LOGGER.debug("Future was completed exceptionally; " + e.getCause());
			return new ResponseEntity<>("User account wasn't received", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			LOGGER.debug("Future was interrupted; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't received", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/{id}/receive")
	@PreAuthorize("hasAuthority('developer:read')")
	public ResponseEntity<?> getAccount(@PathVariable Long id) {
		try {
			UserAccountDto userAccInfo = userAccService.receive(id).get();
			return new ResponseEntity<>(userAccInfo, HttpStatus.OK);
		} catch (CancellationException e) {
			LOGGER.debug("Future completion was unexpectedly cancelled; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't received", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExecutionException e) {
			LOGGER.debug("Future was completed exceptionally; " + e.getCause());
			return new ResponseEntity<>("User account wasn't received", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			LOGGER.debug("Future was interrupted; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't received", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('user:update')")
	public ResponseEntity<?> updateAccount(@RequestBody @Valid UserAccountDtoImpl userAccDto) {
		try {
			userAccService.update(userAccDto).get();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (UsernameNotFoundException e) {
			LOGGER.info("User account wasn't updated; account wasn't found");
			return new ResponseEntity<>("User account wasn't updated", HttpStatus.OK);
		} catch (CancellationException e) {
			LOGGER.debug("Future completion was unexpectedly cancelled; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't updated", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExecutionException e) {
			LOGGER.debug("Future was completed exceptionally; " + e.getCause());
			return new ResponseEntity<>("User account wasn't updated", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			LOGGER.debug("Future was interrupted; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't updated", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/delete")
	@PreAuthorize("hasAuthority('user:delete')")
	public ResponseEntity<?> deleteAccount() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			userAccService.delete(username).get();
			return new ResponseEntity<>(HttpStatus.OK);
			
		} catch (CancellationException e) {
			LOGGER.debug("Future completion was unexpectedly cancelled; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExecutionException e) {
			LOGGER.debug("Future was completed exceptionally; " + e.getCause());
			return new ResponseEntity<>("User account wasn't deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			LOGGER.debug("Future was interrupted; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "{id}/delete")
	@PreAuthorize("hasAuthority('developer:delete')")
	public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
		try {
			userAccService.delete(id).get();
			return new ResponseEntity<>(HttpStatus.OK);
			
		} catch (CancellationException e) {
			LOGGER.debug("Future completion was unexpectedly cancelled; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExecutionException e) {
			LOGGER.debug("Future was completed exceptionally; " + e.getCause());
			return new ResponseEntity<>("User account wasn't deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InterruptedException e) {
			LOGGER.debug("Future was interrupted; " + e.getMessage());
			return new ResponseEntity<>("User account wasn't deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
