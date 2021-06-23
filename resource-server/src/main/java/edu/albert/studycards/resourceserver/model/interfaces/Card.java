package edu.albert.studycards.resourceserver.model.interfaces;

import edu.albert.studycards.resourceserver.model.dto.CardDtoImpl;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;

import java.util.ArrayList;
import java.util.List;

public interface Card {
	
	Long getId();
	void setId(Long id);
	
	String getWord();
	void setWord(String word);
	
	String getWordTr();
	void setWordTr(String wordTr);
	
	void setWordMng(String wordMng);
	String getWordMng();
	

	

}
