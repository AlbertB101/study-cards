package edu.albert.studycards.resourceserver.model.interfaces;

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
	void setLangPack(LangPackPersitent langPack);
}
