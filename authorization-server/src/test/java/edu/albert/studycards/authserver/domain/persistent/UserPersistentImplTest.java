package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.dto.UserDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.UserDto;
import edu.albert.studycards.authserver.domain.interfaces.UserPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserPersistentImplTest {
	private static String testEmail = "TestEmail@mail.com";
	private static String testFirstName = "SomeName";
	private static String testLastName = "SomeLastName";
	private static String  testPass = "TestPassword";
	private static Role testRole = Role.USER;
	private static Status testStatus = Status.ACTIVE;
	
	@Test
	@DisplayName("Should be constructed with ClientDto instance")
	void shouldBeConstructedWithClientDtoInstance() {
		UserDto testUserDto = new UserDtoImpl();
		testUserDto.setEmail(testEmail);
		testUserDto.setFirstName(testFirstName);
		testUserDto.setLastName(testLastName);
		testUserDto.setPassword(testPass);
//		testClientDto.setRole(testRole);
//		testClientDto.setStatus(testStatus);
		
		UserPersistent client = new UserPersistentImpl(testUserDto);
		
		assertEquals(testUserDto.getEmail(), client.getEmail());
		assertEquals(testUserDto.getFirstName(), client.getFirstName());
		assertEquals(testUserDto.getLastName(), client.getLastName());
		assertEquals(testUserDto.getPassword(), client.getPassword());
//		assertNotNull(client.getCreated());
//		assertEquals(testClientDto.getRole(), client.getRole());
//		assertEquals(testClientDto.getStatus(), client.getStatus());
	}
}
