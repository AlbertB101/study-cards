package edu.albert.studycards.authserver.domain.dto;

import edu.albert.studycards.authserver.domain.interfaces.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserAccountDtoImpl implements UserAccountDto {

//TODO: SPLIT into AuthenticationRequest and userAccountDto(and registrationRequest)???
	
	@NotEmpty
	@Size(max = 64)
	private String firstName;
	
	@NotEmpty
	@Size(max = 64)
	private String lastName;
	
//	@NotEmpty
	@Size(max = 256)
	@NaturalId(mutable = true)
	private String email;
	
//	@NotEmpty
	private String password;
	
	@Enumerated(value = EnumType.STRING)
	private Role role;
	
	@Enumerated(value = EnumType.STRING)
	private Status status;
	
	public UserAccountDtoImpl(UserAccountPersistent userAcc) {
		this.firstName = userAcc.getFirstName();
		this.lastName = userAcc.getLastName();
		this.email = userAcc.getEmail();
		this.password = userAcc.getPassword();
		this.role = userAcc.getRole();
		this.status = userAcc.getStatus();
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
