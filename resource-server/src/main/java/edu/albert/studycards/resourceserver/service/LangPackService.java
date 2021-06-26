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
	
	public LangPackDto create(LangPackDto langPackDto) throws LangPackAlreadyExistsException {
		Objects.requireNonNull(langPackDto);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if (langPackRepo.existsByAccountEmailAndLang(email, langPackDto.getLang())) {
			throw new LangPackAlreadyExistsException();
		}
		
		//TODO: add langPackDto validity check
		
		LangPackPersistentImpl langPack = new LangPackPersistentImpl(langPackDto);
		return new LangPackDtoImpl(langPackRepo.saveAndFlush(langPack));
	}
	
	public LangPackDto get(Long id) {
		Objects.requireNonNull(id);
		return new LangPackDtoImpl(find(id));
	}
	
	public LangPackDto get(String lang) {
		Objects.requireNonNull(lang);
		return new LangPackDtoImpl(find(lang));
	}
	
	public LangPackDto update(LangPackDto langPackDto) {
		Objects.requireNonNull(langPackDto);
		LangPackPersistent langPackP = find(langPackDto.getLang());
		updateCards(langPackP, langPackDto);
		//TODO: add update repository query
		LangPackPersistent updatedLangPack = langPackRepo.saveAndFlush((LangPackPersistentImpl) langPackP);
		return new LangPackDtoImpl(updatedLangPack);
	}
	
	private void updateCards(LangPackPersistent to, LangPackDto from) {
		for (CardDto cardDto : from.getCards()) {
			if (to.contains(cardDto.getWord()))
				to.editCard(cardDto);
			else
				to.addCard(new CardPersistentImpl(cardDto, to));
		}
	}
	
	public void delete(Long id) {
		Objects.requireNonNull(id);
		langPackRepo.deleteById(id);
	}
	
	LangPackPersistent find(Long id) {
		Objects.requireNonNull(id);
		return langPackRepo
			       .findById(id)
			       .orElseThrow(NoSuchElementException::new);
	}
	
	LangPackPersistent find(String lang) {
		Objects.requireNonNull(lang);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return langPackRepo
			       .findByAccountEmailAndLang(email, lang)
			       .orElseThrow(NoSuchElementException::new);
	}
}