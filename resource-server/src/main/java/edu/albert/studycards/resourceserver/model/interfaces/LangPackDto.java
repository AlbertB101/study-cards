package edu.albert.studycards.resourceserver.model.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.albert.studycards.resourceserver.model.dto.LangPackDtoImpl;

import java.util.List;

@JsonDeserialize(as = LangPackDtoImpl.class)
public interface LangPackDto extends LangPack{
	Long getAccountId();
	void setAccountId(Long accountId);
	
	void addCard(CardDto card);
	void setCard(CardDto card);
	
	CardDto getCard(int n);
	CardDto getCard(String word);
	CardDto getCard(Long id);
	int indexOf(CardDto card);
	
	void deleteCard(int n);
	void deleteCard(String word);
	
	List<CardDto> getCards();
	void setCards(List<CardDto> givenCards);
	void clearCards();

	int size();
	
	static LangPackDto from(LangPackPersistent langPack) {
		LangPackDto langPackDto = new LangPackDtoImpl();
		langPackDto.setId(langPack.getId());
		langPackDto.setLang(langPack.getLang());
		langPackDto.setAccountId(langPack.getId());
		List<CardDto> dtoList = CardDto.listFrom(langPack.getCards());
		langPackDto.setCards(dtoList);
		
		return langPackDto;
	}
}
