package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.model.dto.CardDtoImpl;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.model.persistent.*;
import edu.albert.studycards.resourceserver.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CardService {
	
	@Autowired
	CardRepository cardRepository;
	@Autowired
	LangPackRepository langPackRepository;
	@Autowired
	LangPackService langPackService;
	
	public void create(CardDto cardDto) {
		try {
			var langPackFromRepository = langPackService.find(
				cardDto.getAccountId(),
				cardDto.getLang());
			CardPersistentImpl card = new CardPersistentImpl(cardDto, langPackFromRepository);
			
			cardRepository.saveAndFlush(card);
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public CardDto get(String word, String lang) throws NoSuchElementException {
		CardPersistent cardPersistent = findCard(word, lang);
		return new CardDtoImpl(cardPersistent);
	}
	
	public CardDto get(String word, String lang, long accountId) throws NoSuchElementException {
		CardPersistent cardPersistent = findCard(word, lang, accountId);
		return new CardDtoImpl(cardPersistent);
	}
	
	@Deprecated
	public void update(CardDto cardDto) throws RuntimeException {
		CardPersistent card = findCard(cardDto.getWord(), cardDto.getLang());
		
		card.setWordTr(cardDto.getWordTr());
		card.setWordMng(cardDto.getWordMng());
		//TODO: add update query to card repo
//		save(card);
	}
	
	public void delete(String word, String lang) throws NoSuchElementException {
		LangPackPersistent langPack = langPackService.find(lang);
		langPack.deleteCard(word);
		langPackRepository.saveAndFlush((LangPackPersistentImpl) langPack);
	}
	
	public ResponseEntity<?> delete(String word, String lang, long accountId) throws NoSuchElementException {
		LangPackPersistent langPack = langPackService.find(accountId, lang);
		langPack.deleteCard(word);
		langPackRepository.saveAndFlush((LangPackPersistentImpl) langPack);
		
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	private CardPersistent findCard(String word, String lang) throws NoSuchElementException {
		LangPackPersistent langPack = langPackService.find(lang);
		return langPack.getCard(word);
	}
	
	private CardPersistent findCard(String word, String lang, long accountId) {
		CardPersistent card = null;
		LangPackPersistent langPack = langPackService.find(accountId, lang);
		try {
			card = langPack.getCard(word);
		} catch (NoSuchElementException e) {
			//TODO: add logging
			System.err.println(e.getClass());
		}
		return card;
	}
}
