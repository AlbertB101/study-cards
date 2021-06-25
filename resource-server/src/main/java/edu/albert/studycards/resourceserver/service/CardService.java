package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.model.dto.CardDtoImpl;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import edu.albert.studycards.resourceserver.model.persistent.LangPackPersistentImpl;
import edu.albert.studycards.resourceserver.repository.CardRepository;
import edu.albert.studycards.resourceserver.repository.LangPackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class CardService {
	
	@Autowired
	CardRepository cardRepo;
	@Autowired
	LangPackRepository langPackRepo;
	@Autowired
	LangPackService langPackService;
	
	public void create(CardDto cardDto) {
		Objects.requireNonNull(cardDto);
		LangPackPersistent langPack = langPackService.find(cardDto.getLang());
		if (!langPack.hasCard(cardDto.getWord())) {
			langPack.addCard(new CardPersistentImpl(cardDto, langPack));
		}
	}
	
	public CardDto get(String word) {
		Objects.requireNonNull(word);
		return new CardDtoImpl(findCard(word));
	}
	
	public void update(CardDto cardDto) {
		Objects.requireNonNull(cardDto);
		CardPersistent card = findCard(cardDto.getWord(), cardDto.getLang());
		card.setWordTr(cardDto.getWordTr());
		card.setWordMng(cardDto.getWordMng());
		//TODO: add update query to card repo
		cardRepo.saveAndFlush((CardPersistentImpl) card);
	}
	
	public void delete(String word, String lang) {
		Objects.requireNonNull(word);
		Objects.requireNonNull(lang);
		LangPackPersistent langPack = langPackService.find(lang);
		langPack.deleteCard(word);
		langPackRepo.saveAndFlush((LangPackPersistentImpl) langPack);
	}
	
	private CardPersistent findCard(String word) {
		Objects.requireNonNull(word);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return cardRepo
			       .findByWordAndLangPack_AccountEmail(word, email)
			       .orElseThrow(NoSuchElementException::new);
	}
	
	private CardPersistent findCard(String word, String lang) {
		Objects.requireNonNull(word);
		Objects.requireNonNull(lang);
		LangPackPersistent langPack = langPackService.find(lang);
		return langPack.getCard(word);
	}
}
