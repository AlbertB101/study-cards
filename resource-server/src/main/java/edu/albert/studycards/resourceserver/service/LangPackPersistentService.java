package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.exceptions.LangPackAlreadyExistsException;
import edu.albert.studycards.resourceserver.model.dto.CardDto;
import edu.albert.studycards.resourceserver.model.dto.LangPackDto;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.model.persistent.*;
import edu.albert.studycards.resourceserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import java.util.Objects;

@Service
public class LangPackPersistentService implements LangPackService {
	
	@Autowired
	LangPackRepository langPackRepo;
	@Autowired
	CardService cardService;
	
	@Override
	public LangPackDto create(LangPackDto langPackDto) throws LangPackAlreadyExistsException {
		Objects.requireNonNull(langPackDto);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if (langPackRepo.existsByAccountEmailAndLang(email, langPackDto.lang())) {
			throw new LangPackAlreadyExistsException();
		}
		
		//TODO: add langPackDto validity check
		
		LangPackPersistentImpl langPack = new LangPackPersistentImpl(langPackDto);
		return new LangPackDto(langPackRepo.saveAndFlush(langPack));
	}
	
	@Override
	public LangPackDto get(Long id) {
		Objects.requireNonNull(id);
		return new LangPackDto(find(id));
	}
	
	@Override
	public LangPackDto get(String lang) {
		Objects.requireNonNull(lang);
		return new LangPackDto(find(lang));
	}
	
	@Override
	public LangPackDto update(LangPackDto langPackDto) {
		Objects.requireNonNull(langPackDto);
		LangPackPersistent langPackP = find(langPackDto.lang());
		langPackP.setLang(langPackDto.lang());
		updateCards(langPackP, langPackDto);
		//TODO: add update repository query
		LangPackPersistent updatedLangPack = langPackRepo.saveAndFlush((LangPackPersistentImpl) langPackP);
		return new LangPackDto(updatedLangPack);
	}
	
	private void updateCards(LangPackPersistent to, LangPackDto from) {
		for (CardDto cardDto : from.cards()) {
			if (to.contains(cardDto.word()))
				to.editCard(cardDto);
			else
				to.addCard(new CardPersistentImpl(cardDto, to));
		}
	}
	
	@Override
	public void delete(Long id) {
		Objects.requireNonNull(id);
		langPackRepo.deleteById(id);
	}
	
	@Override
	public LangPackPersistent find(Long id) {
		Objects.requireNonNull(id);
		return langPackRepo
			       .findById(id)
			       .orElseThrow(NoSuchElementException::new);
	}
	
	@Override
	public LangPackPersistent find(String lang) {
		Objects.requireNonNull(lang);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return langPackRepo
			       .findByAccountEmailAndLang(email, lang)
			       .orElseThrow(NoSuchElementException::new);
	}
}