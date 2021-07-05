package edu.albert.studycards.authserver.domain.dto;

import edu.albert.studycards.authserver.domain.Role;
import edu.albert.studycards.authserver.domain.Status;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

/**
 * Dto object for transferring {@link UserAccountPersistent} information.
 *
 * <p>The properties don't have {@link javax.validation.constraints}
 * because their values may be used in different contexts
 * (for example: we don't need pass firstName and lastName when we just want to update cards).
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class UserAccountDtoImpl implements UserAccountDto {
	
	@Size(max = 64)
	private String firstName;
	
	@Size(max = 64)
	private String lastName;
	
	@Size(max = 256)
	@NaturalId(mutable = true)
	private String email;
	
	private String password;
	
	@Enumerated(value = EnumType.STRING)
	private Role role;
	
	@Enumerated(value = EnumType.STRING)
	private Status status;
	
	/**
	 * Constructor is dedicated for creating new {@link UserAccountDtoImpl} instance
	 * based on {@link UserAccountPersistent} argument. New {@link UserAccountDtoImpl}
	 * instance just copies values from {@link UserAccountPersistent} argument.
	 * Password won't be copied due to security concerns
	 * @param userAccount valid UserAccountPersistent instance
	 */
	public UserAccountDtoImpl(UserAccountPersistent userAccount) {
		this.firstName = userAccount.getFirstName();
		this.lastName = userAccount.getLastName();
		this.email = userAccount.getEmail();
		this.password = null;
		this.role = userAccount.getRole();
		this.status = userAccount.getStatus();
	}
	
	public UserAccountDtoImpl(UserAccountDto userAcc) {
		this.firstName = userAcc.getFirstName();
		this.lastName = userAcc.getLastName();
		this.email = userAcc.getEmail();
		this.password = userAcc.getPassword();
		this.role = userAcc.getRole();
		this.status = userAcc.getStatus();
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
