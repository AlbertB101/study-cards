package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.dto.UserAccountDtoImpl;
import edu.albert.studycards.authserver.domain.dto.UserAccountDto;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistent;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistentImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ClientRepositoryTest {
	private static UserAccountDto userAccountDto;
	private static UserAccountPersistentImpl clientPersistent;
	
	@Autowired
	UserAccountRepository userAccRepo;
	
	@Test
	@BeforeAll
	@DisplayName("Should configure clients")
	static void shouldConfigureClients() {
		userAccountDto = new UserAccountDtoImpl();
		userAccountDto.setEmail("TestEmail@mail.com");
		userAccountDto.setFirstName("SomeName");
		userAccountDto.setLastName("SomeLastName");
		userAccountDto.setPassword("TestPassword");
//		clientDto.setRole(Role.USER);
//		clientDto.setStatus(Status.ACTIVE);
		
		clientPersistent = new UserAccountPersistentImpl(userAccountDto);
	}
	
	@SneakyThrows
	@Test
	@DisplayName("When client is saved should be found by email")
	void whenSavedShouldBeFoundByEmail() {
		userAccRepo.saveAndFlush(clientPersistent);
		
		assertThat(userAccRepo.findByEmail(userAccountDto.getEmail())).isNotNull();
	}
	
	@Test
	@DisplayName("When client isn't saved should return Optional with NULL")
	void whenClientIsNotSavedShouldReturnOptionalWithNull() {
		Optional<UserAccountPersistent> opt = userAccRepo.findByEmail(clientPersistent.getEmail());
		assertFalse(opt.isPresent());
		assertThrows(NoSuchElementException.class, () -> opt.get());
	}
	
	@Test
	@DisplayName("When client exists should return true")
	void whenClientExistsShouldReturnTrue() {
		userAccRepo.saveAndFlush(clientPersistent);
		
		assertTrue(userAccRepo.existsByEmail(clientPersistent.getEmail()));
	}
	
	@Test
	@DisplayName("When client doesn't exist should return false")
	void whenClientDoesntExistShouldReturnFalse() {
		assertFalse(userAccRepo.existsByEmail(clientPersistent.getEmail()));
	}
	
	@Test
	@DisplayName("Should update Client firstName and lastName")
	void shouldUpdateClientFirstNameAndLastName() {
		String newFirstName = "Alex";
		String newLastName = "Johnson";
		userAccRepo.saveAndFlush(clientPersistent);
		
		userAccRepo.updateUserAccByEmail(
			clientPersistent.getEmail(),
			newFirstName,
			newLastName);
		
		UserAccountPersistent updatedClient = userAccRepo
			                                 .findByEmail(ClientRepositoryTest.clientPersistent.getEmail())
			                                 .orElse(new UserAccountPersistentImpl());
		assertEquals(newFirstName, updatedClient.getFirstName());
		assertEquals(newLastName, updatedClient.getLastName());
	}
}
