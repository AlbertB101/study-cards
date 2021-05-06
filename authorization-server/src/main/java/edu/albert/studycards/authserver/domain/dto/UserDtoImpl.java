package edu.albert.studycards.authserver.domain.dto;

import edu.albert.studycards.authserver.domain.interfaces.UserDto;
import edu.albert.studycards.authserver.domain.interfaces.UserPersistent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserDtoImpl implements UserDto {
	
	@NotEmpty
	@Size(max = 64)
	private String firstName;
	
	@NotEmpty
	@Size(max = 64)
	private String lastName;
	
	@NotEmpty
	@Size(max = 256)
	@NaturalId(mutable = true)
	private String email;
	
	@NotEmpty
	private String password;
	
	public UserDtoImpl(UserPersistent client) {
		this.firstName = client.getFirstName();
		this.lastName = client.getLastName();
		this.email = client.getEmail();
		this.password = client.getPassword();
	}
	
	public UserDtoImpl(UserDto client) {
		this.firstName = client.getFirstName();
		this.lastName = client.getLastName();
		this.email = client.getEmail();
		this.password = client.getPassword();
	}
	
	@Override
	public String toString() {
		return "ClientDtoImpl{" +
			       "firstName='" + firstName + '\'' +
			       ", lastName='" + lastName + '\'' +
			       ", email='" + email + '\'' +
			       '}';
	}
}
