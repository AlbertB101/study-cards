package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.ResourceProvider;
import edu.albert.studycards.resourceserver.model.dto.CardDtoImpl;
import edu.albert.studycards.resourceserver.model.interfaces.CardDto;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackDto;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackPersistent;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import edu.albert.studycards.resourceserver.model.persistent.LangPackPersistentImpl;
import edu.albert.studycards.resourceserver.repository.CardRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
	
	private static final ResourceProvider<CardDto> CARD_DTO_RSC_PROVIDER =
		new ResourceProvider<>(CardDto.class);
	private static final ResourceProvider<LangPackDto> LANG_PACK_DTO_RSC_PROVIDER =
		new ResourceProvider<>(LangPackDto.class);
	
	private static final CardDto TEST_CARD_DTO =
		CARD_DTO_RSC_PROVIDER.generateResource();
	private static final LangPackDto TEST_LANG_PACK_DTO =
		LANG_PACK_DTO_RSC_PROVIDER.generateResource();
	
	@InjectMocks
	private CardService cardService;
	@Mock
	private CardRepository cardRepo;
	@Mock
	private LangPackService langPackService;
	
	@BeforeAll
	static void configureSecurityContext() {
		Authentication auth = new UsernamePasswordAuthenticationToken(
			TEST_LANG_PACK_DTO.getAccountEmail(), "");
		SecurityContext securityContext = new SecurityContextImpl(auth);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@AfterAll
	static void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}
	
	@Test
	@DisplayName("create() should throw NullPointerException")
	void createShouldThrowNullPointerException() {
		assertThrows(NullPointerException.class,
			() -> cardService.create(null));
	}
	
	@Test
	@DisplayName("create() should add new card to LangPack")
	void createShouldAddNewCardToLangPack() {
		LangPackPersistent langPackP = new LangPackPersistentImpl(TEST_LANG_PACK_DTO);
		when(langPackService.find(TEST_CARD_DTO.getLang()))
			.thenReturn(langPackP);
		
		cardService.create(TEST_CARD_DTO);
		assertTrue(langPackP.contains(TEST_CARD_DTO.getWord()));
	}
	
	@Disabled
	@Test
	@DisplayName("create() should throw NoSuchElementException when repository doesn't have langPack")
	void createShouldThrowNoSuchElementExceptionWhenRepositoryDoesntHaveLangPack() {
		CardDto cardWithIncorrectLang = new CardDtoImpl(TEST_CARD_DTO);
		cardWithIncorrectLang.setLang("Incorrect lang");
		
		assertThrows(NoSuchElementException.class,
			() -> cardService.create(cardWithIncorrectLang));
	}
	
	@Test
	@DisplayName("get() should throw NullPointerException")
	void getShouldThrowNullPointerException() {
		assertThrows(NullPointerException.class,
			() -> cardService.get(null));
	}
	
	@Test
	@DisplayName("get() should return LangPack")
	void getShouldReturnLangPack() {
		LangPackPersistent langPackP = new LangPackPersistentImpl(TEST_LANG_PACK_DTO);
		when(cardRepo.findByWordAndLangPack_AccountEmail(
			TEST_CARD_DTO.getWord(),
			SecurityContextHolder.getContext().getAuthentication().getName()))
			.thenReturn(Optional.of(new CardPersistentImpl(TEST_CARD_DTO, langPackP)));
		
		assertDoesNotThrow(() -> {
			CardDto cardDto = cardService.get(TEST_CARD_DTO.getWord());
			assertEquals(TEST_CARD_DTO, cardDto);
		});
	}
}
