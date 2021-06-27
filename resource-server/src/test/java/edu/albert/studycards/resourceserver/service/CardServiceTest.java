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
	private LangPackPersistentService langPackService;
	
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
	@DisplayName("get() by card id should throw NullPointerException")
	void getByCardIdShouldThrowNullPointerException() {
		assertThrows(NullPointerException.class,
			() -> cardService.get((Long) null));
	}
	
	@Test
	@DisplayName("get() by LangPack language should throw NullPointerException")
	void getByLangPackLanguageShouldThrowNullPointerException() {
		assertThrows(NullPointerException.class,
			() -> cardService.get((String) null));
	}
	
	@Test
	@DisplayName("get() by card id should return Card")
	void getByCardIdShouldReturnCard() {
		LangPackPersistent langPackP = new LangPackPersistentImpl(TEST_LANG_PACK_DTO);
		when(cardRepo.findById(TEST_CARD_DTO.getId()))
			.thenReturn(Optional.of(new CardPersistentImpl(TEST_CARD_DTO, langPackP)));
		
		assertDoesNotThrow(() -> {
			CardDto receivedCard = cardService.get(TEST_CARD_DTO.getId());
			assertEquals(TEST_CARD_DTO, receivedCard);
		});
	}
	
	@Test
	@DisplayName("get() by LangPack language should return Card")
	void getByLangPackLanguageShouldReturnCard() {
		LangPackPersistent langPackP = new LangPackPersistentImpl(TEST_LANG_PACK_DTO);
		when(cardRepo.findByWordAndLangPack_AccountEmail(
			TEST_CARD_DTO.getWord(),
			SecurityContextHolder.getContext().getAuthentication().getName()))
			.thenReturn(Optional.of(new CardPersistentImpl(TEST_CARD_DTO, langPackP)));
		
		assertDoesNotThrow(() -> {
			CardDto receivedCard = cardService.get(TEST_CARD_DTO.getWord());
			assertEquals(TEST_CARD_DTO, receivedCard);
		});
	}
	
	
	@Test
	@DisplayName("update() should throw NullPointerException")
	void updateShouldThrowNullPointerException() {
		assertThrows(NullPointerException.class,
			() -> cardService.update(null));
	}
	
	@Test
	@DisplayName("update() should return updated CardDto")
	void updateShouldReturnUpdatedCardDto() {
		CardDto updateCard = new CardDtoImpl(TEST_CARD_DTO);
		String newWordTr = "new transcription";
		String newWordMng = "new meaning";
		updateCard.setWordTr(newWordTr);
		updateCard.setWordMng(newWordMng);
		LangPackPersistent langPackP = new LangPackPersistentImpl(TEST_LANG_PACK_DTO);
		when(cardRepo.findByWordAndLangPack_AccountEmail(
			TEST_CARD_DTO.getWord(),
			SecurityContextHolder.getContext().getAuthentication().getName()))
			.thenReturn(Optional.of(new CardPersistentImpl(TEST_CARD_DTO, langPackP)));
		when(cardRepo.saveAndFlush(any()))
			.thenReturn(new CardPersistentImpl(updateCard, langPackP));
		
		assertDoesNotThrow(() -> {
			CardDto updatedCard = cardService.update(updateCard);
			assertNotNull(updatedCard);
			assertEquals(TEST_CARD_DTO.getWord(), updatedCard.getWord());
			assertEquals(newWordTr, updatedCard.getWordTr());
			assertEquals(newWordMng, updatedCard.getWordMng());
		});
	}
	
	@Test
	@DisplayName("delete() with one argument should throw NullPointerException")
	void deleteWithOneArgumentShouldThrowNullPointerException() {
		assertThrows(NullPointerException.class, () -> cardService.delete(null));
	}
	
	@Test
	@DisplayName("delete() with two arguments should throw NullPointerException when first argument is null")
	void deleteWithTwoArgumentsShouldThrowNullPointerExceptionWhenFirstArgumentIsNull() {
		assertThrows(NullPointerException.class,
			() -> cardService.delete(null, TEST_CARD_DTO.getLang()));
	}
	
	@Test
	@DisplayName("delete() with two arguments should throw NullPointerException when second argument is null")
	void deleteWithTwoArgumentsShouldThrowNullPointerExceptionWhenSecondArgumentIsNull() {
		assertThrows(NullPointerException.class,
			() -> cardService.delete(TEST_CARD_DTO.getWord(), null));
	}
	@Test
	@DisplayName("delete() with two arguments should throw NullPointerException when both arguments are null")
	void deleteWithTwoArgumentsShouldThrowNullPointerExceptionWhenBothArgumentsAreNull() {
		assertThrows(NullPointerException.class,
			() -> cardService.delete(TEST_CARD_DTO.getWord(), null));
	}
	
}
