package edu.albert.studycards.resourceserver.model.dto;

import edu.albert.studycards.resourceserver.ResourceProvider;
import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackPersistent;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import edu.albert.studycards.resourceserver.model.persistent.LangPackPersistentImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDtoTest {
	private static final ResourceProvider<CardDto> RESOURCE_PROVIDER = new ResourceProvider<>(CardDto.class);
	private static final CardDto TEST_CARD = RESOURCE_PROVIDER.generateResource();
	
	@Test
	@DisplayName("Should be constructed with word info")
	void shouldBeConstructedWithWordInfo() {
		CardDto cardDto = new CardDto(
			TEST_CARD.word(),
			TEST_CARD.wordTr(),
			TEST_CARD.wordMng());
		
		assertEquals(TEST_CARD.word(), cardDto.word());
		assertEquals(TEST_CARD.wordTr(), cardDto.wordTr());
		assertEquals(TEST_CARD.wordMng(), cardDto.wordMng());
	}
	
//	@Test
//	@DisplayName("Should be constructed with CardDto")
//	void shouldBeConstructedWithCardDto() {
//		CardDto cardDto = new CardDto(TEST_CARD);
//
//		assertEquals(TEST_CARD.id(), cardDto.id());
//		assertEquals(TEST_CARD.word(), cardDto.word());
//		assertEquals(TEST_CARD.wordTr(), cardDto.wordTr());
//		assertEquals(TEST_CARD.wordMng(), cardDto.wordMng());
//		assertEquals(TEST_CARD.lang(), cardDto.lang());
//		assertEquals(TEST_CARD.langPackId(), cardDto.langPackId());
//	}
	
	@Test
	@DisplayName("Should be constructed with CardPersistent")
	void shouldBeConstructedWithCardPersistent() {
		CardPersistent cardP = new CardPersistentImpl();
		LangPackPersistent langPackP = new LangPackPersistentImpl();
		langPackP.setLang(TEST_CARD.lang());
		langPackP.setId(TEST_CARD.langPackId());
		cardP.setId(TEST_CARD.id());
		cardP.setWord(TEST_CARD.word());
		cardP.setWordTr(TEST_CARD.wordTr());
		cardP.setWordMng(TEST_CARD.wordMng());
		cardP.setLangPack(langPackP);
		
		CardDto cardDto = new CardDto(cardP);
		
		assertEquals(TEST_CARD.id(), cardDto.id());
		assertEquals(TEST_CARD.id(), cardDto.id());
		assertEquals(TEST_CARD.lang(), cardDto.lang());
		assertEquals(TEST_CARD.word(), cardDto.word());
		assertEquals(TEST_CARD.wordTr(), cardDto.wordTr());
		assertEquals(TEST_CARD.wordMng(), cardDto.wordMng());
	}
}
