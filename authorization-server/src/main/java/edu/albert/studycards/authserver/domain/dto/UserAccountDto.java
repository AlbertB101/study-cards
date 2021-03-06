package edu.albert.studycards.authserver.domain.dto;

import edu.albert.studycards.authserver.domain.Role;
import edu.albert.studycards.authserver.domain.Status;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistent;
import org.hibernate.annotations.NaturalId;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Albert Banshchikov
 * @since 2021.12.14
 */
public record UserAccountDto(
        @Size(max = 64) String firstName,
        @Size(max = 64) String lastName,
        @Size(max = 256) @NaturalId(mutable = true) String email,
        String password,
        @Enumerated(value = EnumType.STRING) Role role,
        @Enumerated(value = EnumType.STRING) Status status) implements Serializable {

    /**
     * Constructor is dedicated for creating new {@link UserAccountDtoImpl} instance
     * based on {@link UserAccountPersistent} argument. New {@link UserAccountDtoImpl}
     * instance just copies values from {@link UserAccountPersistent} argument.
     * Password won't be copied due to security concerns
     *
     * @param userAccount valid UserAccountPersistent instance
     */
    public UserAccountDto(UserAccountPersistent userAccount) {
        this(userAccount.getFirstName(),
                userAccount.getLastName(),
                userAccount.getEmail(),
                null,
                userAccount.getRole(),
                userAccount.getStatus());

    }

    public UserAccountDto(UserAccountDto userAcc) {
        this(userAcc.firstName(),
                userAcc.lastName(),
                userAcc.email(),
                userAcc.password(),
                userAcc.role(),
                userAcc.status());
    }

    @Override
    public String toString() {
        return "ClientDtoImpl{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
