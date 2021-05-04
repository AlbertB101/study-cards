package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity(name = "Client")
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class ClientPersistentImpl implements ClientPersistent, Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column
	@Size(max = 32)
	private String firstName;
	
	@NotNull
	@Column
	@Size(max = 32)
	private String lastName;
	
	@Size(max = 128)
	@Column(name = "EMAIL")
	@NaturalId(mutable = true)
	private String email;
	
	@NotNull
	@Column
	@Size(max = 64)
	private String password;
	
	public ClientPersistentImpl(ClientDto clientDto) {
		this.email = clientDto.getEmail();
		this.firstName = clientDto.getFirstName();
		this.lastName = clientDto.getLastName();
		this.password = clientDto.getPassword();
	}
}
