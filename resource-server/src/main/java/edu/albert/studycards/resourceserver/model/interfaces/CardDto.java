package edu.albert.studycards.resourceserver.model.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//@JsonDeserialize(as = CardDtoImpl.class)???
public interface CardDto {
	Long getId();
	void setId(Long id);
	
	String getWord();
    void setWord(String word);
    
	String getWordTr();
	void setWordTr(String wordTr);
	
	void setWordMng(String wordMng);
	String getWordMng();
}
