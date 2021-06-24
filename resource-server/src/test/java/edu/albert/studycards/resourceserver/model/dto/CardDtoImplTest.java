package edu.albert.studycards.resourceserver.model.dto;

import edu.albert.studycards.resourceserver.model.interfaces.CardDto;
import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackPersistent;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import edu.albert.studycards.resourceserver.model.persistent.LangPackPersistentImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDtoImplTest {
	private static Long testId = 1L;
	private static String testWord = "Word";
	private static String testWordTr = "WordTr";
	private static String testWordMng = "WordMng";
	private static String testLang = "testLang";
	private static Long testLangPackId = 1L;
	
	@Test
	@DisplayName("Should be constructed with word info")
	void shouldBeConstructedWithWordInfo() {
		CardDto cardDto = new CardDtoImpl(testWord, testWordTr, testWordMng);
		
		assertEquals(testWord, cardDto.getWord());
		assertEquals(testWordTr, cardDto.getWordTr());
		assertEquals(testWordMng, cardDto.getWordMng());
	}
	
	@Test
	@DisplayName("Should be constructed with CardDto")
	void shouldBeConstructedWithCardDto() {
		CardDto cardDto = new CardDtoImpl(testWord, testWordTr, testWordMng);
		cardDto.setId(testId);
		cardDto.setLang(testLang);
		cardDto.setLangPackId(testLangPackId);
		
		assertEquals(testId, cardDto.getId());
		assertEquals(testWord, cardDto.getWord());
		assertEquals(testWordTr, cardDto.getWordTr());
		assertEquals(testWordMng, cardDto.getWordMng());
		assertEquals(testLangPackId, cardDto.getLangPackId());
	}
	
	@Test
	@DisplayName("Should be constructed with CardPersistent")
	void shouldBeConstructedWithCardPersistent() {
		CardPersistent cardP = new CardPersistentImpl();
		LangPackPersistent langPackP = new LangPackPersistentImpl();
		langPackP.setId(testLangPackId);
		cardP.setId(testId);
		cardP.setWord(testWord);
		cardP.setWordTr(testWordTr);
		cardP.setWordMng(testWordMng);
		cardP.setLangPack(langPackP);
		
		CardDto cardDto = new CardDtoImpl(cardP);
		
		assertEquals(testId, cardDto.getId());
		assertEquals(testWord, cardDto.getWord());
		assertEquals(testWordTr, cardDto.getWordTr());
		assertEquals(testWordMng, cardDto.getWordMng());
		assertEquals(testLangPackId, cardDto.getLangPackId());
	}
}
