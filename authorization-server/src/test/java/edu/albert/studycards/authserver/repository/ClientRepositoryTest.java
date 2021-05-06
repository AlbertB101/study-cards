package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.dto.UserDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.UserDto;
import edu.albert.studycards.authserver.domain.interfaces.UserPersistent;
import edu.albert.studycards.authserver.domain.persistent.UserPersistentImpl;
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
	private static UserDto userDto;
	private static UserPersistentImpl clientPersistent;
	
	@Autowired
	ClientRepository clientRepo;
	
	@Test
	@BeforeAll
	@DisplayName("Should configure clients")
	static void shouldConfigureClients() {
		userDto = new UserDtoImpl();
		userDto.setEmail("TestEmail@mail.com");
		userDto.setFirstName("SomeName");
		userDto.setLastName("SomeLastName");
		userDto.setPassword("TestPassword");
//		clientDto.setRole(Role.USER);
//		clientDto.setStatus(Status.ACTIVE);
		
		clientPersistent = new UserPersistentImpl(userDto);
	}
	
	@SneakyThrows
	@Test
	@DisplayName("When client is saved should be found by email")
	void whenSavedShouldBeFoundByEmail() {
		clientRepo.saveAndFlush(clientPersistent);
		
		assertThat(clientRepo.findByEmail(userDto.getEmail())).isNotNull();
	}
	
	@Test
	@DisplayName("When client isn't saved should return Optional with NULL")
	void whenClientIsNotSavedShouldReturnOptionalWithNull() {
		Optional<UserPersistent> opt = clientRepo.findByEmail(clientPersistent.getEmail());
		assertFalse(opt.isPresent());
		assertThrows(NoSuchElementException.class, () -> opt.get());
	}
	
	@Test
	@DisplayName("When client exists should return true")
	void whenClientExistsShouldReturnTrue() {
		clientRepo.saveAndFlush(clientPersistent);
		
		assertTrue(clientRepo.existsByEmail(clientPersistent.getEmail()));
	}
	
	@Test
	@DisplayName("When client doesn't exist should return false")
	void whenClientDoesntExistShouldReturnFalse() {
		assertFalse(clientRepo.existsByEmail(clientPersistent.getEmail()));
	}
	
	@Test
	@DisplayName("Should update Client firstName and lastName")
	void shouldUpdateClientFirstNameAndLastName() {
		String newFirstName = "Alex";
		String newLastName = "Johnson";
		clientRepo.saveAndFlush(clientPersistent);
		
		clientRepo.updateClientByEmail(
			clientPersistent.getEmail(),
			newFirstName,
			newLastName);
		
		UserPersistent updatedClient = clientRepo
			                                 .findByEmail(ClientRepositoryTest.clientPersistent.getEmail())
			                                 .orElse(new UserPersistentImpl());
		assertEquals(newFirstName, updatedClient.getFirstName());
		assertEquals(newLastName, updatedClient.getLastName());
	}
}
