package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.exceptions.LangPackAlreadyExistsException;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.model.persistent.*;
import edu.albert.studycards.resourceserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LangPackService {
	
	@Autowired
	LangPackRepository langPackRepo;
	@Autowired
	CardService cardService;
	
	public void create(LangPackDto langPackDto) throws LangPackAlreadyExistsException {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if (langPackRepo.existsByAccountEmailAndLang(email, langPackDto.getLang())) {
			throw new LangPackAlreadyExistsException();
		}
		
		LangPackPersistentImpl langPack = new LangPackPersistentImpl(langPackDto);
		langPackRepo.saveAndFlush(langPack);
	}
	
	public LangPackDto get(String lang) {
		return LangPackDto.from(find(lang));
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
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return langPackRepo
			       .findByAccountEmailAndLang(email, lang)
			       .orElseThrow(NoSuchElementException::new);
	}
}