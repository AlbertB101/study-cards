package edu.albert.studycards.authserver.domain.dto;

import edu.albert.studycards.authserver.domain.interfaces.UserDto;
import edu.albert.studycards.authserver.domain.interfaces.UserPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.Status;
import edu.albert.studycards.authserver.domain.persistent.UserPersistentImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDtoImplTest {
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
		
		UserDto client = new UserDtoImpl(testUserDto);
		
		assertEquals(testUserDto.getEmail(), client.getEmail());
		assertEquals(testUserDto.getFirstName(), client.getFirstName());
		assertEquals(testUserDto.getLastName(), client.getLastName());
		assertEquals(testUserDto.getPassword(), client.getPassword());
//		assertEquals(testClientDto.getRole(), client.getRole());
//		assertEquals(testClientDto.getStatus(), client.getStatus());
	}
	
	@Test
	@DisplayName("Should be constructed with ClientPersistent instance")
	void shouldBeConstructedWithClientPersistentInstance() {
		UserPersistent testUserPersistent = new UserPersistentImpl();
		testUserPersistent.setEmail(testEmail);
		testUserPersistent.setFirstName(testFirstName);
		testUserPersistent.setLastName(testLastName);
		testUserPersistent.setPassword(testPass);
//		testClientPersistent.setRole(testRole);
//		testClientPersistent.setStatus(testStatus);
		
		UserDto client = new UserDtoImpl(testUserPersistent);
		
		assertEquals(testUserPersistent.getEmail(), client.getEmail());
		assertEquals(testUserPersistent.getFirstName(), client.getFirstName());
		assertEquals(testUserPersistent.getLastName(), client.getLastName());
		assertEquals(testUserPersistent.getPassword(), client.getPassword());
//		assertEquals(testClientPersistent.getRole(), client.getRole());
//		assertEquals(testClientPersistent.getStatus(), client.getStatus());
	}
	
}
