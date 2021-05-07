package edu.albert.studycards.authserver.rest;

import edu.albert.studycards.authserver.domain.dto.UserAccountDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountDto;
import edu.albert.studycards.authserver.service.UserAccountService;
import javassist.bytecode.stackmap.TypeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/account")
public class UserAccountController {
	
	final Logger logger = LoggerFactory.getLogger(UserAccountController.class);
	
	@Autowired
	UserAccountService userAccService;
	
	@PostMapping(value = "/signUp")
	public ResponseEntity<?> signUp(@RequestBody @Valid UserAccountDtoImpl userAccDto) {
		try {
			UserAccountDto newUserAcc = userAccService.register(userAccDto).get();
			return new ResponseEntity<>(
				Map.of("UserAccInfo", newUserAcc,
					"ResponseMessage", "Client was successfully registered"),
				HttpStatus.OK);
		} catch (Throwable e) {
			logger.error("Exception while future completion. " + e.getMessage());
			return new ResponseEntity<>("new user account wasn't created", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<?> getAccount() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			UserAccountDto userAccInfo = userAccService.receive(username).get();
			return new ResponseEntity<>(userAccInfo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}
	
	@GetMapping(value = "/{id}/info")
	@PreAuthorize("hasAuthority('developer:read')")
	public ResponseEntity<?> getAccount(@PathVariable Long id) {
		try {
			UserAccountDto accInf = userAccService.receive(id).get();
			return new ResponseEntity<>(accInf, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception while future completion/n" + e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
		
	}
	
	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('user:update')")
	public ResponseEntity<?> updateAccount(@RequestBody @Valid UserAccountDtoImpl userAccDto) {
		try {
			userAccService.update(userAccDto).get();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}
	
	@PostMapping(value = "/delete")
	@PreAuthorize("hasAuthority('user:delete')")
	public ResponseEntity<?> deleteAccount() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			userAccService.delete(username);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}
	
	@PostMapping(value = "{id}/delete")
	@PreAuthorize("hasAuthority('developer:delete')")
	public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
		try {
			userAccService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}
}
