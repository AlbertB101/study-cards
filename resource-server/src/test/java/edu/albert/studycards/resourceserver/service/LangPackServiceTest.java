package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.ResourceProvider;
import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackDto;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackPersistent;
import edu.albert.studycards.resourceserver.model.persistent.LangPackPersistentImpl;
import edu.albert.studycards.resourceserver.repository.LangPackRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LangPackServiceTest {
	
	private static final ResourceProvider<LangPackDto> LANG_PACK_DTO_RSC_PROVIDER =
		new ResourceProvider<>(LangPackDto.class);
	private static final LangPackDto TEST_LANG_PACK =
		LANG_PACK_DTO_RSC_PROVIDER.generateResource();
	
	@InjectMocks
	private LangPackService langPackService;
	@Mock
	private LangPackRepository langPackRepo;
	@Mock
	private CardService cardService;
	
	@BeforeAll
	static void configureSecurityContext() {
		Authentication auth = new UsernamePasswordAuthenticationToken(
			TEST_LANG_PACK.getAccountEmail(),
			"Credentials");
		SecurityContext securityContext = new SecurityContextImpl(auth);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@AfterAll
	static void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}
	
	@Test
	@DisplayName("Should create new LangPack")
	void shouldCreateNewLangPack() {
		when(langPackRepo.existsByAccountEmailAndLang(
			TEST_LANG_PACK.getAccountEmail(),
			TEST_LANG_PACK.getLang()))
			.thenReturn(false);
		when(langPackRepo.saveAndFlush(any()))
			.thenReturn(new LangPackPersistentImpl(TEST_LANG_PACK));
		
		assertDoesNotThrow(() -> {
			LangPackPersistent langPackP = langPackService.create(TEST_LANG_PACK);
			assertNotNull(langPackP);
			assertEquals(TEST_LANG_PACK.getLang(), langPackP.getLang());
			assertEquals(TEST_LANG_PACK.getAccountEmail(), langPackP.getAccountEmail());
			List<CardPersistent> cards = CardPersistent.listFrom(TEST_LANG_PACK.getCards(), langPackP);
			assertEquals(cards, langPackP.getCards());
		});
	}
	
	
}
