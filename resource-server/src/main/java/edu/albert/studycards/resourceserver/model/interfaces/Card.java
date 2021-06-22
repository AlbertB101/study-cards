package edu.albert.studycards.resourceserver.model.interfaces;

import edu.albert.studycards.resourceserver.model.dto.CardDtoImpl;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;

import java.util.ArrayList;
import java.util.List;

public interface Card {
	
	static List<CardDto> from(List<CardPersistent> givenCards) {
		List<CardDto> result = new ArrayList<>(givenCards.size());
		for (CardPersistent cardP : givenCards) {
			CardDto card = new CardDtoImpl(cardP);
			result.add(card);
		}
		return result;
	}
	
	static List<CardPersistent> from(List<CardDto> cards, LangPackPersistent langPack) {
		List<CardPersistent> result = new ArrayList<>(cards.size());
		for (CardDto cardDto : cards) {
			CardPersistent card = new CardPersistentImpl(cardDto, langPack);
			result.add(card);
		}
		return result;
	}
}
