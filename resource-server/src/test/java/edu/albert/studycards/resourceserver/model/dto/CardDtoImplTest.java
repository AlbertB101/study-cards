package edu.albert.studycards.resourceserver.model.dto;

import edu.albert.studycards.resourceserver.ResourceProvider;
import edu.albert.studycards.resourceserver.model.interfaces.CardDto;
import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackPersistent;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import edu.albert.studycards.resourceserver.model.persistent.LangPackPersistentImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDtoImplTest {
	private static final ResourceProvider<CardDto> RESOURCE_PROVIDER = new ResourceProvider<>(CardDto.class);
	private static final CardDto TEST_CARD = RESOURCE_PROVIDER.generateResource();
	
	@Test
	@DisplayName("Should be constructed with word info")
	void shouldBeConstructedWithWordInfo() {
		CardDto cardDto = new CardDtoImpl(
			TEST_CARD.getWord(),
			TEST_CARD.getWordTr(),
			TEST_CARD.getWordMng());
		
		assertEquals(TEST_CARD.getWord(), cardDto.getWord());
		assertEquals(TEST_CARD.getWordTr(), cardDto.getWordTr());
		assertEquals(TEST_CARD.getWordMng(), cardDto.getWordMng());
	}
	
	@Test
	@DisplayName("Should be constructed with CardDto")
	void shouldBeConstructedWithCardDto() {
		CardDto cardDto = new CardDtoImpl(TEST_CARD);
		
		assertEquals(TEST_CARD.getId(), cardDto.getId());
		assertEquals(TEST_CARD.getWord(), cardDto.getWord());
		assertEquals(TEST_CARD.getWordTr(), cardDto.getWordTr());
		assertEquals(TEST_CARD.getWordMng(), cardDto.getWordMng());
		assertEquals(TEST_CARD.getLang(), cardDto.getLang());
		assertEquals(TEST_CARD.getLangPackId(), cardDto.getLangPackId());
	}
	
	@Test
	@DisplayName("Should be constructed with CardPersistent")
	void shouldBeConstructedWithCardPersistent() {
		CardPersistent cardP = new CardPersistentImpl();
		LangPackPersistent langPackP = new LangPackPersistentImpl();
		langPackP.setLang(TEST_CARD.getLang());
		langPackP.setId(TEST_CARD.getLangPackId());
		cardP.setId(TEST_CARD.getId());
		cardP.setWord(TEST_CARD.getWord());
		cardP.setWordTr(TEST_CARD.getWordTr());
		cardP.setWordMng(TEST_CARD.getWordMng());
		cardP.setLangPack(langPackP);
		
		CardDto cardDto = new CardDtoImpl(cardP);
		
		assertEquals(TEST_CARD.getId(), cardDto.getId());
		assertEquals(TEST_CARD.getLangPackId(), cardDto.getLangPackId());
		assertEquals(TEST_CARD.getLang(), cardDto.getLang());
		assertEquals(TEST_CARD.getWord(), cardDto.getWord());
		assertEquals(TEST_CARD.getWordTr(), cardDto.getWordTr());
		assertEquals(TEST_CARD.getWordMng(), cardDto.getWordMng());
	}
}
