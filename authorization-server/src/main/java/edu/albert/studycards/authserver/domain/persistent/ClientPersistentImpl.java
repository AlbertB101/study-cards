package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.interfaces.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Client")
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class ClientPersistentImpl implements ClientPersistent, Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@OneToOne(
//		mappedBy = "client",
//		cascade = CascadeType.ALL,
//		orphanRemoval = true,
//		fetch = FetchType.LAZY
//	)
//	private AccountPersistentImpl account;
	
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
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date created;
	
	public ClientPersistentImpl(ClientDto clientDto) {
		this.email = clientDto.getEmail();
		this.firstName = clientDto.getFirstName();
		this.lastName = clientDto.getLastName();
		this.password = clientDto.getPassword();
		this.created = new Date();
	}
}
