package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.dto.UserAccountDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountDto;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserAccountPersistentImplTest {
	private static String testEmail = "TestEmail@mail.com";
	private static String testFirstName = "SomeName";
	private static String testLastName = "SomeLastName";
	private static String  testPass = "TestPassword";
	private static Role testRole = Role.USER;
	private static Status testStatus = Status.ACTIVE;
	
	@Test
	@DisplayName("Should be constructed with ClientDto instance")
	void shouldBeConstructedWithClientDtoInstance() {
		UserAccountDto testUserAccountDto = new UserAccountDtoImpl();
		testUserAccountDto.setEmail(testEmail);
		testUserAccountDto.setFirstName(testFirstName);
		testUserAccountDto.setLastName(testLastName);
		testUserAccountDto.setPassword(testPass);
//		testClientDto.setRole(testRole);
//		testClientDto.setStatus(testStatus);
		
		UserAccountPersistent client = new UserAccountPersistentImpl(testUserAccountDto);
		
		assertEquals(testUserAccountDto.getEmail(), client.getEmail());
		assertEquals(testUserAccountDto.getFirstName(), client.getFirstName());
		assertEquals(testUserAccountDto.getLastName(), client.getLastName());
		assertEquals(testUserAccountDto.getPassword(), client.getPassword());
//		assertNotNull(client.getCreated());
//		assertEquals(testClientDto.getRole(), client.getRole());
//		assertEquals(testClientDto.getStatus(), client.getStatus());
	}
}
