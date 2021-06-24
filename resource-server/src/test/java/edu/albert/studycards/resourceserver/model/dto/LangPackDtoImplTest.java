package edu.albert.studycards.resourceserver.model.dto;

import edu.albert.studycards.resourceserver.ResourceProvider;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LangPackDtoImplTest {
	private static final ResourceProvider<LangPackDto> RESOURCE_PROVIDER = new ResourceProvider<>(LangPackDto.class);
	private static final LangPackDto TEST_LANG_PACK = RESOURCE_PROVIDER.generateResource();
	
	
	@Test
	@DisplayName("Should be constructed with lang")
	void shouldBeConstructedWithLang() {
		LangPackDto langPackDto = new LangPackDtoImpl(TEST_LANG_PACK.getLang());
		
		assertEquals(TEST_LANG_PACK.getLang(), langPackDto.getLang());
	}
	
	@Test
	@DisplayName("Should be constructed with LangPackDto")
	void shouldBeConstructedWithLangPackDto() {
		LangPackDto langPackDto = new LangPackDtoImpl(TEST_LANG_PACK);
		
		assertEquals(TEST_LANG_PACK.getId(), langPackDto.getId());
		assertEquals(TEST_LANG_PACK.getAccountId(), langPackDto.getAccountId());
		assertEquals(TEST_LANG_PACK.getAccountEmail(), langPackDto.getAccountEmail());
		assertEquals(TEST_LANG_PACK.getLang(), langPackDto.getLang());
		assertEquals(TEST_LANG_PACK.getCards(), langPackDto.getCards());
	}
}
