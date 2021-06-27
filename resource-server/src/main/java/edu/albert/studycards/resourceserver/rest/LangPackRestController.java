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
    
    private static final String RUNTIME_EXCEPTION_RESPONSE_MESSAGE =
    "Input values are incorrect or internal server error";
    
    @Autowired
    private LangPackService langPackService;
    
    /**
     * Creates new {@link LangPackPersistent}.
     *
     * <p>Request should be authorized and have "user" authorities.
     * <p>Request to this endpoint must contain such parameters:
     * <ul>
     * 	<li> accountEmail - the email of account that owns this LangPack;
     * 	<li> lang - language of this LangPack.
     * </ul>
     * Also LangPacks may have "cards" parameter that contains
     * list of {@link CardDto} that will be created and saved within the LangPack.
     *
     * <p>Example:
     * <pre>
     * {@code
     * {
     *   "accountEmail": "email@mail.ru",
     *   "lang": "English",
     *   "cards": [
     *     {
     *       "word": "outstanding",
     *       "wordTr": "ˌaʊtˈstæn.dɪŋ",
     *       "wordMng": "выдающийся"
     *     },
     *     {
     *       "word": "garbage",
     *       "wordTr": "ˈɡɑː.bɪdʒ",
     *       "wordMng": "мусор"
     *     }
     *   ]
     * }
     * }
     * </pre>
     * @param langPack LangPackDto implementation
     * @return ResponseEntity with http status code and just created LangPack
     */
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody @Valid LangPackDtoImpl langPack) {
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
                "LangPack wasn't created." + RUNTIME_EXCEPTION_RESPONSE_MESSAGE,
                HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Returns information about LangPack and its cards. This endpoint allows
     * client to receive account's LangPack.
     *
     * <p>Request should be authorized and have "user" authorities.
     * <p>Request should contain language of LangPack that will be returned.
     * @param lang of LangPack
     * @return ResponseEntity with http status code and LangPack
     */
    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> receive(@RequestParam(name = "lang") String lang) {
        try {
            LangPackDto receivedLangPack = langPackService.get(lang);
            Map<String, Object> response = Map.of(
                "ResponseMessage", "LangPack was successfully created",
                "Card", receivedLangPack);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "LangPack wasn't created." + RUNTIME_EXCEPTION_RESPONSE_MESSAGE,
                HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * This endpoint allows to receive LangPack by its id.
     *
     * <p>Request should be authorized and have "developer" authorities.
     * @param id of LangPackPersistent
     * @return ResponseEntity with http status code and LangPack
     */
    @PreAuthorize("hasAuthority('developer:read')")
    @GetMapping(value = "/receive/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> receive(@PathVariable() Long id) {
        try {
            LangPackDto receivedLangPack = langPackService.get(id);
            return new ResponseEntity<>(receivedLangPack, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "LangPack wasn't updated." + RUNTIME_EXCEPTION_RESPONSE_MESSAGE,
                HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * This endpoint allows to update LangPack language and cards.
     *
     * <p>Request should be authorized and have "user" authorities.
     * <p>Update request may contain such parameters:
     * <ul>
     * 	<li> lang - new language of this LangPack.
     * 	<li> cards - list of {@link CardDto}.
     * 	If {@link LangPackPersistent} doesn't contain a card this card will be added.
     * 	If LangPack already contains the card, word transcription and word meaning will be updated.
     * </ul>
     *
     * <p>Example:
     * <pre>
     * {@code
     * {
     *   "lang": "English",
     *   "cards": [
     *     {
     *       "word": "outstanding",
     *       "wordTr": "ˌaʊtˈstæn.dɪŋ",
     *       "wordMng": "выдающийся"
     *     },
     *     {
     *       "word": "garbage",
     *       "wordTr": "ˈɡɑː.bɪdʒ",
     *       "wordMng": "мусор"
     *     }
     *   ]
     * }
     * }
     * </pre>
     * @param langPackDtoImpl that contains new language and/or cards that should be updated
     * @return ResponseEntity with http status code and updated LangPack
     */
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<?> update(@RequestBody @Valid LangPackDtoImpl langPackDtoImpl) {
        try {
            LangPackDto updatedLangPack = langPackService.update(langPackDtoImpl);
            Map<String, Object> response = Map.of(
                "ResponseMessage", "LangPack was successfully created",
                "Card", updatedLangPack);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "LangPack wasn't updated." + RUNTIME_EXCEPTION_RESPONSE_MESSAGE,
                HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * This endpoint allows to delete LangPack from database.
     *
     * <p>Request should be authorized and have "user" authorities.
     * @param id of LangPack that should be deleted.
     * @return ResponseEntity with http status code and additional information
     */
    @PostMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            langPackService.delete(id);
            return new ResponseEntity<>("LangPack was successfully deleted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                "LangPack wasn't updated." + RUNTIME_EXCEPTION_RESPONSE_MESSAGE,
                HttpStatus.BAD_REQUEST);
        }
    }
}
