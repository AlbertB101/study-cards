package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.dto.ClientDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientPersistentImplTest {
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
		
		ClientPersistent client = new ClientPersistentImpl(testClientDto);
		
		assertEquals(testClientDto.getEmail(), client.getEmail());
		assertEquals(testClientDto.getFirstName(), client.getFirstName());
		assertEquals(testClientDto.getLastName(), client.getLastName());
		assertEquals(testClientDto.getPassword(), client.getPassword());
		assertEquals(testClientDto.getRole(), client.getRole());
		assertEquals(testClientDto.getStatus(), client.getStatus());
	}
}
