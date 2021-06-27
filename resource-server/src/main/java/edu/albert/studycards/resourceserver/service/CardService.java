package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.model.interfaces.CardDto;
import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;

public interface CardService {
	CardDto create(CardDto cardDto);
	
	CardDto get(Long cardId);
	
	CardDto get(String word);
	
	CardDto update(CardDto cardDto);
	
	void delete(Long cardId);
	
	void delete(String word, String lang);
	
	CardPersistent findCard(String word);
	
	CardPersistent findCard(String word, String lang);
}
