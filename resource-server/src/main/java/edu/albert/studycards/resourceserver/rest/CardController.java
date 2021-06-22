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

//TODO: Make RespEntities return more precise info
@RestController
@RequestMapping("api/v1/card")
public class CardController {
    
    @Autowired
    CardService cardService;
    
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> create(@RequestBody @Valid CardDtoImpl cardDto) {
        try {
            cardService.create(cardDto);
            return new ResponseEntity<>("Card was successfully created", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Card wasn't created", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<?> get(@RequestParam(name = "word") String word,
                                 @RequestParam(name = "lang") String lang) {
        try {
            CardDto cardDto = cardService.get(word, lang);
            return new ResponseEntity<>(cardDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Card wasn't updated", HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<?> update(@RequestBody @Valid CardDtoImpl cardDto) {
        try {
            cardService.update(cardDto);
            return new ResponseEntity<>("Card was successfully created", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Card wasn't updated", HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(value = "/delete")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> delete(@RequestParam("word") String word,
                                    @RequestParam(name = "lang") String lang) {
        try {
            cardService.delete(word, lang);
            return new ResponseEntity<>("Card was successfully deleted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Card wasn't deleted", HttpStatus.NOT_FOUND);
        }
    }
    
}
