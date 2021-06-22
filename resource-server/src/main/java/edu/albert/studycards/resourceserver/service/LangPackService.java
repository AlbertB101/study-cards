package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.model.persistent.*;
import edu.albert.studycards.resourceserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LangPackService {

    @Autowired
    LangPackRepository langPackRepository;
    @Autowired
    CardService cardService;
    @Autowired
    AccountService accountService;
    
    public void create(LangPackDto langPackDto) throws RuntimeException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountPersistent account = accountService.find(auth.getName());
        
        if (langPackRepository.existsByAccountAndLang(account, langPackDto.getLang())) {
            throw new RuntimeException("LangPack already exists");
        }
        
        LangPackPersistentImpl langPack = new LangPackPersistentImpl();
        langPack.setAccount(account);
        langPack.setLang(langPackDto.getLang());
        langPack.setCards(Card.from(langPackDto.getCards(), langPack));
        langPackRepository.saveAndFlush(langPack);
    }
    
    public LangPackDto get(String lang, Long accountId) {
        LangPackPersistent langPackFromRepository = find(accountId, lang);
        if (langPackFromRepository != null)
            return LangPack.persistentToDto(langPackFromRepository);
        else return null;
    }
    
    public LangPackDto get(String lang) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountPersistent acc = accountService.find(username);
        
        LangPackPersistent langPackFromRepository = find(acc.getId(), lang);
        if (langPackFromRepository != null)
            return LangPack.persistentToDto(langPackFromRepository);
        else return null;
    }
    
    public List<String> getExistingLanguages(Long id) throws NoSuchElementException {
        List<String> langs = new ArrayList<>();
        langPackRepository.findByAccount_Id(id)
            .orElseThrow(() -> new NoSuchElementException("No LangPacks"))
            .forEach(lpp -> langs.add(lpp.getLang()));
        return langs;
    }
    
    public void update(LangPackDto langPackDto) throws NoSuchElementException {
        LangPackPersistent langPack = find(
            langPackDto.getAccountId(),
            langPackDto.getLang());
        
        for (CardDto cardDto : langPackDto.getCards()) {
            if (langPack.exists(cardDto.getWord()))
                langPack.editCard(cardDto);
            else
                langPack.addCard(new CardPersistentImpl(cardDto, langPack));
        }
        
        //TODO: add update repository query
//        saveLangPack(langPack);
    }
    
    public void delete(Long accountId, String lang) throws NoSuchElementException {
        AccountPersistent account = accountService.find(accountId);
        langPackRepository.deleteLangPackImplByAccountAndLang(account, lang);
    }
    
    public void delete(String username, String lang) throws NoSuchElementException {
        AccountPersistent account = accountService.find(username);
        langPackRepository.deleteLangPackImplByAccountAndLang(account, lang);
    }
    
    private boolean exists(LangPackDto langPackDtoImpl) {
        if (langPackRepository.existsByLang(langPackDtoImpl.getLang())) {
            return true;
        }
        return false;
    }
    
    LangPackPersistent find(Long accountId, String lang) throws NoSuchElementException {
        return langPackRepository.findByAccount_IdAndLang(accountId, lang)
                   .orElseThrow(() -> new NoSuchElementException("No such LangPack"));
    }
    
    LangPackPersistent find(String lang) throws NoSuchElementException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountPersistent account = accountService.find(username);
        return langPackRepository.findByAccount_IdAndLang(account.getId(), lang)
                   .orElseThrow(() -> new NoSuchElementException("No such LangPack"));
    }
}