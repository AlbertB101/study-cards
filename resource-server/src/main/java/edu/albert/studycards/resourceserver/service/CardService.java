package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.model.dto.CardDtoImpl;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import edu.albert.studycards.resourceserver.repository.CardRepository;
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
	LangPackService langPackService;
	
	public void create(CardDto cardDto) {
		Objects.requireNonNull(cardDto);
		LangPackPersistent langPack = langPackService.find(cardDto.getLang());
		if (!langPack.contains(cardDto.getWord())) {
			langPack.addCard(new CardPersistentImpl(cardDto, langPack));
		}
	}
	
	public CardDto get(String word) {
		Objects.requireNonNull(word);
		return new CardDtoImpl(findCard(word));
	}
	
	public CardDto update(CardDto cardDto) {
		Objects.requireNonNull(cardDto);
		CardPersistent card = findCard(cardDto.getWord());
		card.setWordTr(cardDto.getWordTr());
		card.setWordMng(cardDto.getWordMng());
		//TODO: add update query to card repo
		CardPersistent cardP = cardRepo.saveAndFlush((CardPersistentImpl) card);
		return new CardDtoImpl(cardP);
	}
	
	public void delete(String word, String lang) {
		Objects.requireNonNull(word);
		Objects.requireNonNull(lang);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		cardRepo.deleteByWordAndLangPack_LangAndLangPack_AccountEmail(
			word, lang, email);
	}
	
	CardPersistent findCard(String word) {
		Objects.requireNonNull(word);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return cardRepo
			       .findByWordAndLangPack_AccountEmail(word, email)
			       .orElseThrow(NoSuchElementException::new);
	}
	
	CardPersistent findCard(String word, String lang) {
		Objects.requireNonNull(word);
		Objects.requireNonNull(lang);
		LangPackPersistent langPack = langPackService.find(lang);
		return langPack.getCard(word);
	}
}
