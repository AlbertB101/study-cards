package edu.albert.studycards.authserver.domain.dto;

import edu.albert.studycards.authserver.domain.Role;
import edu.albert.studycards.authserver.domain.Status;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistent;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistentImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAccountDtoImplTest {
	private static String testEmail = "TestEmail@mail.com";
	private static String testFirstName = "SomeName";
	private static String testLastName = "SomeLastName";
	private static String  testPass = "TestPassword";
	private static Role testRole = Role.USER;
	private static Status testStatus = Status.ACTIVE;
	
//	@Test
//	@DisplayName("Should be constructed with ClientDto instance")
//	void shouldBeConstructedWithClientDtoInstance() {
//		UserAccountDto testUserAccountDto = new UserAccountDtoImpl();
//		testUserAccountDto.setEmail(testEmail);
//		testUserAccountDto.setFirstName(testFirstName);
//		testUserAccountDto.setLastName(testLastName);
//		testUserAccountDto.setPassword(testPass);
////		testClientDto.setRole(testRole);
////		testClientDto.setStatus(testStatus);
//
//		UserAccountDto client = new UserAccountDtoImpl(testUserAccountDto);
//
//		assertEquals(testUserAccountDto.getEmail(), client.getEmail());
//		assertEquals(testUserAccountDto.getFirstName(), client.getFirstName());
//		assertEquals(testUserAccountDto.getLastName(), client.getLastName());
//		assertEquals(testUserAccountDto.getPassword(), client.getPassword());
////		assertEquals(testClientDto.getRole(), client.getRole());
////		assertEquals(testClientDto.getStatus(), client.getStatus());
//	}
//
//	@Test
//	@DisplayName("Should be constructed with ClientPersistent instance")
//	void shouldBeConstructedWithClientPersistentInstance() {
//		UserAccountPersistent testUserAccountPersistent = new UserAccountPersistentImpl();
//		testUserAccountPersistent.setEmail(testEmail);
//		testUserAccountPersistent.setFirstName(testFirstName);
//		testUserAccountPersistent.setLastName(testLastName);
//		testUserAccountPersistent.setPassword(testPass);
////		testClientPersistent.setRole(testRole);
////		testClientPersistent.setStatus(testStatus);
//
//		UserAccountDto client = new UserAccountDtoImpl(testUserAccountPersistent);
//
//		assertEquals(testUserAccountPersistent.getEmail(), client.getEmail());
//		assertEquals(testUserAccountPersistent.getFirstName(), client.getFirstName());
//		assertEquals(testUserAccountPersistent.getLastName(), client.getLastName());
//		assertEquals(testUserAccountPersistent.getPassword(), client.getPassword());
////		assertEquals(testClientPersistent.getRole(), client.getRole());
////		assertEquals(testClientPersistent.getStatus(), client.getStatus());
//	}
	
}
