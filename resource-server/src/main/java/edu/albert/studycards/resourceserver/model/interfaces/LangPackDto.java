package edu.albert.studycards.resourceserver.model.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

//@JsonDeserialize(as = LangPackDtoImpl.class)???
public interface LangPackDto {
	Long getAccountId();
	void setAccountId(Long accountId);
	
	Long getId();
	void setId(Long id);
	
	void addCard(CardDto card);
	
	String getLang();
	CardDto getCard(int n);
	CardDto getCard(String word);
	CardDto getCard(Long id);
	List<CardDto> getCards();
	int getIndexOf(CardDto card);
	
	void setLang(String lang);
	void setCards(List<CardDto> givenCards);
	
	void editLang(String lang);
	void editWord(int n, String str);
	void editWordTr(int n, String str);
	void editWordMeaning(int n, String str);
	
	void deleteCard(int n);
	void deleteCard(String word);
	
	boolean exists(String word);
	int size();
}
