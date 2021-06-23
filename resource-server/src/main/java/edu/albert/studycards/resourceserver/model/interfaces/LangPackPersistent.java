package edu.albert.studycards.resourceserver.model.interfaces;


import java.util.List;

public interface LangPackPersistent extends LangPack{
	void addCard(CardPersistent card);
	void setCard(CardPersistent card);
	void editCard(CardDto cardDto);
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
}
