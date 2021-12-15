package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.model.dto.CardDto;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import edu.albert.studycards.resourceserver.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class CardPersistentService implements CardService {
	
	@Autowired
	CardRepository cardRepo;
	@Autowired
	LangPackService langPackService;
	
	@Override
	public CardDto create(CardDto cardDto) {
		Objects.requireNonNull(cardDto);
		LangPackPersistent langPack = langPackService.find(cardDto.lang());
		if (!langPack.contains(cardDto.word())) {
			langPack.addCard(new CardPersistentImpl(cardDto, langPack));
		}
		return new CardDto(langPack.getCard(cardDto.word()));
	}
	
	@Override
	public CardDto get(Long cardId) {
		Objects.requireNonNull(cardId);
		CardPersistent cardP = cardRepo
			                       .findById(cardId)
			                       .orElseThrow(NoSuchElementException::new);
		return new CardDto(cardP);
	}
	
	@Override
	public CardDto get(String word) {
		Objects.requireNonNull(word);
		return new CardDto(findCard(word));
	}
	
	@Override
	public CardDto update(CardDto cardDto) {
		Objects.requireNonNull(cardDto);
		CardPersistent card = findCard(cardDto.word());
		card.setWordTr(cardDto.wordTr());
		card.setWordMng(cardDto.wordMng());
		//TODO: add update query to card repo
		CardPersistent cardP = cardRepo.saveAndFlush((CardPersistentImpl) card);
		return new CardDto(cardP);
	}
	
	@Override
	public void delete(Long cardId) {
		Objects.requireNonNull(cardId);
		cardRepo.deleteById(cardId);
	}
	
	@Override
	public void delete(String word, String lang) {
		Objects.requireNonNull(word);
		Objects.requireNonNull(lang);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		cardRepo.deleteByWordAndLangPack_LangAndLangPack_AccountEmail(
			word, lang, email);
	}
	
	@Override
	public CardPersistent findCard(String word) {
		Objects.requireNonNull(word);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return cardRepo
			       .findByWordAndLangPack_AccountEmail(word, email)
			       .orElseThrow(NoSuchElementException::new);
	}
	
	@Override
	public CardPersistent findCard(String word, String lang) {
		Objects.requireNonNull(word);
		Objects.requireNonNull(lang);
		LangPackPersistent langPack = langPackService.find(lang);
		return langPack.getCard(word);
	}
}
