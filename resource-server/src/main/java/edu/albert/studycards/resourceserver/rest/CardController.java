package edu.albert.studycards.resourceserver.rest;

import edu.albert.studycards.resourceserver.model.dto.CardDtoImpl;
import edu.albert.studycards.resourceserver.model.interfaces.CardDto;
import edu.albert.studycards.resourceserver.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("api/v1/card")
public class CardController {
	
	@Autowired
	CardService cardService;
	
	@PostMapping(value = "/create")
	@PreAuthorize("hasAuthority('user:write')")
	public ResponseEntity<?> create(@RequestBody @Valid CardDtoImpl cardDto) {
		try {
			CardDto createdCard = cardService.create(cardDto);
			Map<String, Object> response = Map.of(
				"ResponseMessage", "Card was successfully created",
				"Card", createdCard);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(
				"Card wasn't created. Incorrect input value or internal server error",
				HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<?> get(@RequestParam(name = "word") String word) {
		try {
			CardDto receivedCard = cardService.get(word);
			Map<String, Object> response = Map.of(
				"ResponseMessage", "Card was successfully received",
				"Card", receivedCard);
			return new ResponseEntity<>(receivedCard, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(
				"Card wasn't found. Incorrect input value or internal server error",
				HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('user:update')")
	public ResponseEntity<?> update(@RequestBody @Valid CardDtoImpl cardDto) {
		try {
			CardDto updatedCard = cardService.update(cardDto);
			Map<String, Object> response = Map.of(
				"ResponseMessage", "Card was successfully received",
				"Card", updatedCard);
			return new ResponseEntity<>(updatedCard, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(
				"Card wasn't updated. Incorrect input value or internal server error",
				HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/delete")
	@PreAuthorize("hasAuthority('user:delete')")
	public ResponseEntity<?> delete(@RequestParam("word") String word,
	                                @RequestParam("lang") String lang) {
		try {
			cardService.delete(word, lang);
			return new ResponseEntity<>("Card was successfully deleted", HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(
				"Card wasn't deleted. Incorrect input value or internal server error",
				HttpStatus.BAD_REQUEST);
		}
	}
    
    @PostMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            cardService.delete(id);
            return new ResponseEntity<>("Card was successfully deleted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "Card wasn't deleted. Incorrect input value or internal server error",
                HttpStatus.BAD_REQUEST);
        }
    }
}