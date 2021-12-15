package edu.albert.studycards.resourceserver.model.interfaces;


import edu.albert.studycards.resourceserver.model.dto.CardDto;

import java.util.List;

public interface LangPackPersistent {
		Long getId();
	void setId(Long id);

	String getLang();
	void setLang(String lang);

	String getAccountEmail();
	void setAccountEmail(String accountEmail);

	void addCard(CardPersistent card);
	void setCard(CardPersistent card);
	CardPersistent getCard(int n);
	CardPersistent getCard(String word);
	CardPersistent getCard(Long id);
	int indexOf(CardPersistent card);

	void deleteCard(int n);
	void deleteCard(String word);

	List<CardPersistent> getCards();
	void setCards(List<CardPersistent> givenCards);
	void clearCards();

	int size();
	boolean contains(String word);

	void editCard(CardDto cardDto);
}
