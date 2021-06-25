package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.exceptions.LangPackAlreadyExistsException;
import edu.albert.studycards.resourceserver.model.dto.LangPackDtoImpl;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.model.persistent.*;
import edu.albert.studycards.resourceserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import java.util.Objects;

@Service
public class LangPackService {
	
	@Autowired
	LangPackRepository langPackRepo;
	@Autowired
	CardService cardService;
	
	public LangPackPersistent create(LangPackDto langPackDto) throws LangPackAlreadyExistsException {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if (langPackRepo.existsByAccountEmailAndLang(email, langPackDto.getLang())) {
			throw new LangPackAlreadyExistsException();
		}
		
		//TODO: add langPackDto validity check
		
		LangPackPersistentImpl langPack = new LangPackPersistentImpl(langPackDto);
		return langPackRepo.saveAndFlush(langPack);
	}
	
	public LangPackDto get(String lang) throws IllegalArgumentException, NoSuchElementException {
		if (lang == null || lang.isBlank())
			throw new IllegalArgumentException("lang value is incorrect");
		return new LangPackDtoImpl(find(lang));
	}
	
	public void update(LangPackDto langPackDto) throws NoSuchElementException {
		LangPackPersistent langPack = find(langPackDto.getLang());
		
		for (CardDto cardDto : langPackDto.getCards()) {
            if (langPack.hasCard(cardDto.getWord()))
				langPack.editCard(cardDto);
			else
				langPack.addCard(new CardPersistentImpl(cardDto, langPack));
		}
		
		langPackRepo.saveAndFlush((LangPackPersistentImpl) langPack);
		//TODO: add update repository query
	}
    
    public void delete(Long id) throws NoSuchElementException {
        langPackRepo.deleteById(id);
    }
	
	LangPackPersistent find(String lang) throws NoSuchElementException {
		Objects.requireNonNull(lang);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return langPackRepo
			       .findByAccountEmailAndLang(email, lang)
			       .orElseThrow(NoSuchElementException::new);
	}
}