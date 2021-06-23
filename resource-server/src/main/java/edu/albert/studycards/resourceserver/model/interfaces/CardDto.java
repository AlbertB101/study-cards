package edu.albert.studycards.resourceserver.model.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.albert.studycards.resourceserver.model.dto.CardDtoImpl;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(as = CardDtoImpl.class)
public interface CardDto extends Card{
	
	Long getLangPackId();
	void setLangPackId(Long id);
	
	Long getAccountId();
	void setAccountId(Long id);
	
	String getLang();
	void setLang(String lang);
	
	static List<CardDto> listFrom(List<CardPersistent> givenCards) {
		List<CardDto> result = new ArrayList<>(givenCards.size());
		for (CardPersistent cardP : givenCards) {
			CardDto card = new CardDtoImpl(cardP);
			result.add(card);
		}
		return result;
	}
}
