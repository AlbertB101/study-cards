package edu.albert.studycards.authserver.domain.dto;

import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.Status;
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
public class ClientDtoImpl implements ClientDto {
	
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
	
	@Enumerated(value = EnumType.STRING)
	private Role role;
	
	@Enumerated(value = EnumType.STRING)
	private Status status;
	
	public ClientDtoImpl(ClientPersistent client) {
		this.firstName = client.getFirstName();
		this.lastName = client.getLastName();
		this.email = client.getEmail();
		this.password = client.getPassword();
		this.role = client.getRole();
		this.status = client.getStatus();
	}
	
	public ClientDtoImpl(ClientDto client) {
		this.firstName = client.getFirstName();
		this.lastName = client.getLastName();
		this.email = client.getEmail();
		this.password = client.getPassword();
		this.role = client.getRole();
		this.status = client.getStatus();
	}
}
