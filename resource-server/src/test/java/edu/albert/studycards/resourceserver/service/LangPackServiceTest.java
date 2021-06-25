package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.ResourceProvider;
import edu.albert.studycards.resourceserver.exceptions.LangPackAlreadyExistsException;
import edu.albert.studycards.resourceserver.model.dto.LangPackDtoImpl;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LangPackServiceTest {
	
	private static final ResourceProvider<LangPackDto> LANG_PACK_DTO_RSC_PROVIDER =
		new ResourceProvider<>(LangPackDto.class);
	private static final LangPackDto TEST_LANG_PACK_DTO =
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
			TEST_LANG_PACK_DTO.getAccountEmail(),
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
			TEST_LANG_PACK_DTO.getAccountEmail(),
			TEST_LANG_PACK_DTO.getLang()))
			.thenReturn(false);
		when(langPackRepo.saveAndFlush(any()))
			.thenReturn(new LangPackPersistentImpl(TEST_LANG_PACK_DTO));
		
		assertDoesNotThrow(() -> {
			LangPackPersistent langPackP = langPackService.create(TEST_LANG_PACK_DTO);
			assertNotNull(langPackP);
			assertEquals(TEST_LANG_PACK_DTO.getLang(), langPackP.getLang());
			assertEquals(TEST_LANG_PACK_DTO.getAccountEmail(), langPackP.getAccountEmail());
			List<CardPersistent> cards = CardPersistent.listFrom(TEST_LANG_PACK_DTO.getCards(), langPackP);
			assertEquals(cards, langPackP.getCards());
		});
	}
	
	@Test
	@DisplayName("Should throw LangPackAlreadyExistsException when create method called")
	void shouldThrowLangPackAlreadyExistsExceptionWhenCreateMethodCalled() {
		when(langPackRepo.existsByAccountEmailAndLang(
			TEST_LANG_PACK_DTO.getAccountEmail(),
			TEST_LANG_PACK_DTO.getLang()))
			.thenReturn(true);
		
		assertThrows(LangPackAlreadyExistsException.class,
			() -> langPackService.create(TEST_LANG_PACK_DTO));
	}
	
	@Test
	@DisplayName("Should throw NullPointerException when create method called with invalid LangPackDto instance")
	void shouldThrowNullPointerExceptionWhenCreateMethodCalledWithInvalidLangPackDtoInstance() {
		when(langPackRepo.existsByAccountEmailAndLang(
			TEST_LANG_PACK_DTO.getAccountEmail(),
			TEST_LANG_PACK_DTO.getLang()))
			.thenReturn(false);
		
		LangPackDto invalidLangPackDto = new LangPackDtoImpl(TEST_LANG_PACK_DTO);
		invalidLangPackDto.setLang(null);
		
		assertThrows(NullPointerException.class, () -> langPackService.create(invalidLangPackDto));
	}
	
	@Test
	@DisplayName("Should return LangPackDto when get method called")
	void shouldReturnLangPackDtoWhenGetMethodCalled() {
		when(langPackRepo.findByAccountEmailAndLang(
			TEST_LANG_PACK_DTO.getAccountEmail(),
			TEST_LANG_PACK_DTO.getLang()))
			.thenReturn(Optional.of(new LangPackPersistentImpl(TEST_LANG_PACK_DTO)));
		
		assertDoesNotThrow(() -> {
			LangPackDto langPackDto = langPackService.get(TEST_LANG_PACK_DTO.getLang());
			assertNotNull(langPackDto);
			assertEquals(TEST_LANG_PACK_DTO, langPackDto);
		});
	}
	
	@Test
	@DisplayName("Should throw IllegalArgumentException when get method called with illegal argument")
	void shouldThrowNullPointerExceptionWhenGetMethodCalledWithIllegalArgument() {
		assertThrows(IllegalArgumentException.class,
			() -> langPackService.get(""));
		assertThrows(IllegalArgumentException.class,
			() -> langPackService.get(null));
	}
	
	@Test
	@DisplayName("get() should throw NoSuchElementException when repository doesn't contain LangPack")
	void getShouldThrowNoSuchElementExceptionWhenRepositoryDoesnTContainLangPack() {
		assertThrows(NoSuchElementException.class, () -> langPackService.get("Some language value"));
	}
	
	@Test
	@DisplayName("update() should throw IllegalArgumentException when argument is null")
	void updateShouldThrowIllegalArgumentExceptionWhenArgumentIsNull() {
		assertThrows(IllegalArgumentException.class,
			() -> langPackService.update(null));
	}
	
	@Test
	@DisplayName("update() should throw NoSuchElementException when repository doesn't contain a LangPack")
	void updateShouldThrowNoSuchElementExceptionWhenRepositoryDoesnTContainALangPack() {
		assertThrows(NoSuchElementException.class,
			() -> langPackService.update(TEST_LANG_PACK_DTO));
	}
	
	@Test
	@DisplayName("update() should return updated LangPack")
	void updateShouldReturnUpdatedLangPack() {
		LangPackPersistentImpl emptyLangPackP = new LangPackPersistentImpl(TEST_LANG_PACK_DTO);
		emptyLangPackP.clearCards();
		
		when(langPackRepo.findByAccountEmailAndLang(
			TEST_LANG_PACK_DTO.getAccountEmail(),
			TEST_LANG_PACK_DTO.getLang()))
			.thenReturn(Optional.of(emptyLangPackP));
		when(langPackRepo.saveAndFlush(emptyLangPackP))
			.thenReturn(new LangPackPersistentImpl(TEST_LANG_PACK_DTO));
		
		assertDoesNotThrow(() -> {
			LangPackDto updated = langPackService.update(TEST_LANG_PACK_DTO);
			assertNotNull(updated);
			assertEquals(TEST_LANG_PACK_DTO, updated);
		});
	}
}
