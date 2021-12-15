package edu.albert.studycards.resourceserver.model.dto;

import edu.albert.studycards.resourceserver.ResourceProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LangPackDtoTest {
	private static final ResourceProvider<LangPackDto> RESOURCE_PROVIDER = new ResourceProvider<>(LangPackDto.class);
	private static final LangPackDto TEST_LANG_PACK = RESOURCE_PROVIDER.generateResource();
	
	
//	@Test
//	@DisplayName("Should be constructed with lang")
//	void shouldBeConstructedWithLang() {
//		LangPackDto langPackDto = new LangPackDto(TEST_LANG_PACK.lang());
//
//		assertEquals(TEST_LANG_PACK.lang(), langPackDto.lang());
//	}
	
//	@Test
//	@DisplayName("Should be constructed with LangPackDto")
//	void shouldBeConstructedWithLangPackDto() {
//		LangPackDto langPackDto = new LangPackDto(TEST_LANG_PACK);
//
//		assertEquals(TEST_LANG_PACK.getId(), langPackDto.getId());
//		assertEquals(TEST_LANG_PACK.getAccountId(), langPackDto.getAccountId());
//		assertEquals(TEST_LANG_PACK.getAccountEmail(), langPackDto.getAccountEmail());
//		assertEquals(TEST_LANG_PACK.lang(), langPackDto.lang());
//		assertEquals(TEST_LANG_PACK.getCards(), langPackDto.getCards());
//	}
}
