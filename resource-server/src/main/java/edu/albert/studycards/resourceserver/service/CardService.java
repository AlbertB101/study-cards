package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.model.dto.CardDtoImpl;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import edu.albert.studycards.resourceserver.model.persistent.LangPackPersistentImpl;
import edu.albert.studycards.resourceserver.repository.CardRepository;
import edu.albert.studycards.resourceserver.repository.LangPackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CardService {
	
	@Autowired
	CardRepository cardRepo;
	@Autowired
	LangPackRepository langPackRepo;
	@Autowired
	LangPackService langPackService;
	
	public void create(CardDto cardDto) {
		LangPackPersistent langPack = langPackService.find(cardDto.getLang());
		CardPersistent card = null;
		if (!langPack.hasCard(cardDto.getWord())) {
			card = new CardPersistentImpl(cardDto, langPack);
		}
		cardRepo.saveAndFlush((CardPersistentImpl) card);
	}
	
	public CardDto get(String word, String lang) throws NoSuchElementException {
		CardPersistent cardPersistent = findCard(word, lang);
		return new CardDtoImpl(cardPersistent);
	}
	
	public void update(CardDto cardDto) throws RuntimeException {
		CardPersistent card = findCard(cardDto.getWord(), cardDto.getLang());
		card.setWordTr(cardDto.getWordTr());
		card.setWordMng(cardDto.getWordMng());
		//TODO: add update query to card repo
		cardRepo.saveAndFlush((CardPersistentImpl) card);
	}
	
	public void delete(String word, String lang) throws NoSuchElementException {
		LangPackPersistent langPack = langPackService.find(lang);
		langPack.deleteCard(word);
		langPackRepo.saveAndFlush((LangPackPersistentImpl) langPack);
	}
	
	private CardPersistent findCard(String word, String lang) throws NoSuchElementException {
		LangPackPersistent langPack = langPackService.find(lang);
		return langPack.getCard(word);
	}
	
	private CardPersistent findCard(String word, String lang, long accountId) throws NoSuchElementException {
		LangPackPersistent langPack = langPackService.find(accountId, lang);
		return langPack.getCard(word);
	}
}
