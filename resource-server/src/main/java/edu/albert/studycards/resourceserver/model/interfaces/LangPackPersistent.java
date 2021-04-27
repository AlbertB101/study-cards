package edu.albert.studycards.resourceserver.model.interfaces;


import java.util.List;

public interface LangPackPersistent {
	Long getId();
	void setId(Long id);
	
	Long getAccountId();
	void setAccountId(Long accountId);
	AccountPersistent getAccount();
	void setAccount(AccountPersistent account);
	
	
	String getLang();
	void setLang(String lang);
	void editLang(String lang);
	
	
	void addCard(CardPersistent card);
	void editCard(CardDto cardDto);
	void setCards(List<CardPersistent> cards);
	
	CardPersistent getCard(int n);
	CardPersistent getCard(String word);
	CardPersistent getCard(Long id);
	List<CardPersistent> getCards();
	int getIndexOf(CardPersistent card);
	
	void editWord(int n, String str);
	void editWordTr(int n, String str);
	void editWordMeaning(int n, String str);
	
	void deleteCard(int n);
	void deleteCard(String word);
	
	int size();
	void clear();
	boolean exists(String word);
}
