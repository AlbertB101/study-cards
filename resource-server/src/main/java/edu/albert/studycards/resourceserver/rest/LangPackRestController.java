package edu.albert.studycards.resourceserver.rest;

import edu.albert.studycards.resourceserver.exceptions.LangPackAlreadyExistsException;
import edu.albert.studycards.resourceserver.model.dto.LangPackDtoImpl;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.service.LangPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * Rest controller for managing {@link LangPackPersistent}. This controller exposes endpoints
 * for CRUD operations.
 *
 * <p>Request to all endpoints should be authenticated. Some endpoints
 * require different authorization levels.
 * Some endpoints are available for those who has "user" authorities and
 * some for those who has"developer" authorities.
 *
 * <p>All endpoints return {@link ResponseEntity}.
 * {@link ResponseEntity} contains http status code and message with
 * additional information about request processing.
 *
 * Endpoints receive data and return data in JSON files.
 */
@RestController
@RequestMapping("/api/v1/langPack")
public class LangPackRestController {
    
    private static final String RESPONSE_MSG_FOR_RUNTIME_EXC =
    "Input values are incorrect or internal server error";
    
    @Autowired
    private LangPackService langPackService;
    
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createLangPack(@RequestBody @Valid LangPackDtoImpl langPack) {
        try {
            LangPackDto createdLangPack = langPackService.create(langPack);
            Map<String, Object> response = Map.of(
                "ResponseMessage", "LangPack was successfully created",
                "Card", createdLangPack);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (LangPackAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "LangPack wasn't created." + RESPONSE_MSG_FOR_RUNTIME_EXC,
                HttpStatus.BAD_REQUEST);
        }
    }
    
    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLangPack(@RequestParam(name = "lang") String lang) {
        try {
            LangPackDto receivedLangPack = langPackService.get(lang);
            Map<String, Object> response = Map.of(
                "ResponseMessage", "LangPack was successfully created",
                "Card", receivedLangPack);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "LangPack wasn't created." + RESPONSE_MSG_FOR_RUNTIME_EXC,
                HttpStatus.BAD_REQUEST);
        }
    }
    
    @PreAuthorize("hasAuthority('developer:read')")
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLangPack(@PathVariable() Long id) {
        try {
            LangPackDto receivedLangPack = langPackService.get(id);
            return new ResponseEntity<>(receivedLangPack, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "LangPack wasn't updated." + RESPONSE_MSG_FOR_RUNTIME_EXC,
                HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<?> updateLangPack(@RequestBody @Valid LangPackDtoImpl langPackDtoImpl) {
        try {
            LangPackDto updatedLangPack = langPackService.update(langPackDtoImpl);
            Map<String, Object> response = Map.of(
                "ResponseMessage", "LangPack was successfully created",
                "Card", updatedLangPack);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "LangPack wasn't updated." + RESPONSE_MSG_FOR_RUNTIME_EXC,
                HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> deleteLangPack(@PathVariable Long id) {
        try {
            langPackService.delete(id);
            return new ResponseEntity<>("LangPack was successfully deleted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "LangPack wasn't updated." + RESPONSE_MSG_FOR_RUNTIME_EXC,
                HttpStatus.BAD_REQUEST);
        }
    }
}
