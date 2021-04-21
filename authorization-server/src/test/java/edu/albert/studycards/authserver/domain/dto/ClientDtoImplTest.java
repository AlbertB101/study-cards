package edu.albert.studycards.authserver.domain.dto;

import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.Status;
import edu.albert.studycards.authserver.domain.persistent.ClientPersistentImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientDtoImplTest {
	private static String testEmail = "TestEmail@mail.com";
	private static String testFirstName = "SomeName";
	private static String testLastName = "SomeLastName";
	private static String  testPass = "TestPassword";
	private static Role testRole = Role.USER;
	private static Status testStatus = Status.ACTIVE;
	
	@Test
	@DisplayName("Should be constructed with ClientDto instance")
	void shouldBeConstructedWithClientDtoInstance() {
		ClientDto testClientDto = new ClientDtoImpl();
		testClientDto.setEmail(testEmail);
		testClientDto.setFirstName(testFirstName);
		testClientDto.setLastName(testLastName);
		testClientDto.setPassword(testPass);
		testClientDto.setRole(testRole);
		testClientDto.setStatus(testStatus);
		
		ClientDto client = new ClientDtoImpl(testClientDto);
		
		assertEquals(testClientDto.getEmail(), client.getEmail());
		assertEquals(testClientDto.getFirstName(), client.getFirstName());
		assertEquals(testClientDto.getLastName(), client.getLastName());
		assertEquals(testClientDto.getPassword(), client.getPassword());
		assertEquals(testClientDto.getRole(), client.getRole());
		assertEquals(testClientDto.getStatus(), client.getStatus());
	}
	
	@Test
	@DisplayName("Should be constructed with ClientPersistent instance")
	void shouldBeConstructedWithClientPersistentInstance() {
		ClientPersistent testClientPersistent = new ClientPersistentImpl();
		testClientPersistent.setEmail(testEmail);
		testClientPersistent.setFirstName(testFirstName);
		testClientPersistent.setLastName(testLastName);
		testClientPersistent.setPassword(testPass);
		testClientPersistent.setRole(testRole);
		testClientPersistent.setStatus(testStatus);
		
		ClientDto client = new ClientDtoImpl(testClientPersistent);
		
		assertEquals(testClientPersistent.getEmail(), client.getEmail());
		assertEquals(testClientPersistent.getFirstName(), client.getFirstName());
		assertEquals(testClientPersistent.getLastName(), client.getLastName());
		assertEquals(testClientPersistent.getPassword(), client.getPassword());
		assertEquals(testClientPersistent.getRole(), client.getRole());
		assertEquals(testClientPersistent.getStatus(), client.getStatus());
	}
	
}
