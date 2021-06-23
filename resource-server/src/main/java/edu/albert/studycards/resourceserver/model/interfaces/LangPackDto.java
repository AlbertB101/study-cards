package edu.albert.studycards.resourceserver.model.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.albert.studycards.resourceserver.model.dto.LangPackDtoImpl;

import java.util.List;

@JsonDeserialize(as = LangPackDtoImpl.class)
public interface LangPackDto extends LangPack{
	
	void addCard(CardDto card);
	
	CardDto getCard(int n);
	CardDto getCard(String word);
	CardDto getCard(Long id);
	List<CardDto> getCards();
	int getIndexOf(CardDto card);
	
	void setCards(List<CardDto> givenCards);
	
	void editLang(String lang);
	void editWord(int n, String str);
	void editWordTr(int n, String str);
	void editWordMeaning(int n, String str);
	
	void deleteCard(int n);
	void deleteCard(String word);
	
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
