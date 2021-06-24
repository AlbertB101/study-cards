package edu.albert.studycards.resourceserver.rest;

import edu.albert.studycards.resourceserver.exceptions.LangPackAlreadyExistsException;
import edu.albert.studycards.resourceserver.model.dto.LangPackDtoImpl;
import edu.albert.studycards.resourceserver.service.LangPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

//TODO: Make RespEntities return more precise info
@RestController
@RequestMapping("/api/v1/langPack")
public class LangPackRestController {
    
    @Autowired
    private LangPackService langPackService;
    
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createLangPack(@RequestBody @Valid LangPackDtoImpl langPack) {
        try {
            langPackService.create(langPack);
        } catch (RuntimeException | LangPackAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLangPack(@RequestParam(name = "lang") String lang) {
        try {
            return new ResponseEntity<>(langPackService.get(lang), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    
//    @PreAuthorize("hasAuthority('developer:read')")
//    @GetMapping(value = "/{id}/get", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getLangPack(@PathVariable() Long id,
//                                         @RequestParam(name = "lang") String lang) {
//        try {
//            return new ResponseEntity<>(langPackService.get(id, lang), HttpStatus.OK);
//        } catch (NoSuchElementException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
//        }
//    }
    
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<?> updateLangPack(@RequestBody @Valid LangPackDtoImpl langPackDtoImpl) {
        try {
            langPackService.update(langPackDtoImpl);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping(value = "/delete")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<?> deleteLangPack(@RequestParam("lang") String lang) {
        try {
//            langPackService.delete();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
//    @PreAuthorize("hasAuthority('developer:read')")
//    @GetMapping(value = "/getAccountLangPackLanguages", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getExistingLanguages(@RequestParam(name = "accountId") Long accountId) {
//        try {
//            return new ResponseEntity<>(langPackService.getExistingLanguages(accountId), HttpStatus.OK);
//        } catch (NoSuchElementException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
//        }
//    }
}
