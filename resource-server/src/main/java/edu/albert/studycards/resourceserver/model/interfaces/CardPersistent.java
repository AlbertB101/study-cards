package edu.albert.studycards.resourceserver.model.interfaces;

import edu.albert.studycards.resourceserver.model.dto.CardDto;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;

import java.util.ArrayList;
import java.util.List;

public interface CardPersistent {

	Long getId();
	void setId(Long id);

	String getWord();
	void setWord(String word);

	String getWordTr();
	void setWordTr(String wordTr);

	void setWordMng(String wordMng);
	String getWordMng();

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
