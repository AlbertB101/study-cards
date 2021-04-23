package edu.albert.studycards.authserver.rest;

import edu.albert.studycards.authserver.domain.dto.ClientDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1")
public class AuthorizationController {
	
	@Autowired
	ClientService clientService;
	
	@PostMapping(value = "/registration")
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
}
