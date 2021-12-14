package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.Role;
import edu.albert.studycards.authserver.domain.Status;
import edu.albert.studycards.authserver.domain.dto.UserAccountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserAccountPersistentImplTest {
    private static String testEmail = "TestEmail@mail.com";
    private static String testFirstName = "SomeName";
    private static String testLastName = "SomeLastName";
    private static String testPass = "TestPassword";
    private static Role testRole = Role.USER;
    private static Status testStatus = Status.ACTIVE;

    @Test
    @DisplayName("Should be constructed with ClientDto instance")
    void shouldBeConstructedWithClientDtoInstance() {
        final UserAccountDto testUserAccountDto = new UserAccountDto(
                testFirstName,
                testLastName,
                testEmail,
                testPass,
                testRole,
                testStatus
        );
        UserAccountPersistent client = new UserAccountPersistentImpl(testUserAccountDto);

        assertEquals(testUserAccountDto.email(), client.getEmail());
        assertEquals(testUserAccountDto.firstName(), client.getFirstName());
        assertEquals(testUserAccountDto.lastName(), client.getLastName());
        assertEquals(testUserAccountDto.password(), client.getPassword());
//		assertNotNull(client.getCreated());
//		assertEquals(testClientDto.getRole(), client.getRole());
//		assertEquals(testClientDto.getStatus(), client.getStatus());
    }
}
