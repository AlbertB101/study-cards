package edu.albert.studycards.resourceserver.model.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.albert.studycards.resourceserver.model.dto.LangPackDtoImpl;

import java.util.List;

@JsonDeserialize(as = LangPackDtoImpl.class)
public interface LangPackDto extends LangPack{
	
	void addCard(CardDto card);
	void setCard(CardDto card);
	
	CardDto getCard(int n);
	CardDto getCard(String word);
	CardDto getCard(Long id);
	int getIndexOf(CardDto card);
	
	void deleteCard(int n);
	void deleteCard(String word);
	
	List<CardDto> getCards();
	void setCards(List<CardDto> givenCards);
	void clearCards();
	
	boolean exists(String word);
	int size();
	
	static LangPackDto from(LangPackPersistent langPack) {
		LangPackDto langPackDto = new LangPackDtoImpl();
		langPackDto.setId(langPack.getId());
		langPackDto.setLang(langPack.getLang());
		langPackDto.setAccountId(langPack.getAccountId());
		List<CardDto> dtoList = CardDto.listFrom(langPack.getCards());
		langPackDto.setCards(dtoList);
		
		return langPackDto;
	}
}
