package edu.albert.studycards.resourceserver.model.interfaces;

import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;

import java.util.ArrayList;
import java.util.List;

public interface CardPersistent extends Card{
	
	LangPackPersistent getLangPack();
	void setLangPack(LangPackPersistent langPack);
	
	static List<CardPersistent> listFrom(List<CardDto> cards, LangPackPersistent langPack) {
		List<CardPersistent> result = new ArrayList<>(cards.size());
		for (CardDto cardDto : cards) {
			CardPersistent card = new CardPersistentImpl(cardDto, langPack);
			result.add(card);
		}
		return result;
	}
}
