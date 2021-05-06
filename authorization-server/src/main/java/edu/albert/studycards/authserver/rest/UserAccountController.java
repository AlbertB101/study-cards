package edu.albert.studycards.authserver.rest;

import edu.albert.studycards.authserver.domain.dto.UserAccountDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountDto;
import edu.albert.studycards.authserver.repository.UserAccountRepository;
import edu.albert.studycards.authserver.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class UserAccountController {
	
	@Autowired
	UserAccountService userAccountService;
	
	@PostMapping(value = "/signUp")
	public ResponseEntity<?> signUp(@RequestBody @Valid UserAccountDtoImpl clientDto) {
		try {
			CompletableFuture<UserAccountDto> compFuture = userAccountService.registerClient(clientDto);
			UserAccountDto registeredClient = compFuture.join();
			return new ResponseEntity<>(
				Map.of("ClientMetaInfo", registeredClient,
					"ResponseMessage", "Client was successfully registered"),
				HttpStatus.OK);
		} catch (Throwable e) {
			return new ResponseEntity<>("Client wasn't registered. " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
