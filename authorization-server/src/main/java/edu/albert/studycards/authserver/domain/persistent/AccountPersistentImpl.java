package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.interfaces.AccountPersistent;
import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Account")
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
public class AccountPersistentImpl implements AccountPersistent, Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "role")
	private Role role;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@OneToOne(
		optional = false,
		targetEntity = ClientPersistentImpl.class
	)
	@JoinColumn(name = "client_email", referencedColumnName = "email")
	private ClientPersistent client;
	
	public AccountPersistentImpl(ClientPersistent client) {
		this.client = client;
		this.role = Role.USER;
		this.status = Status.ACTIVE;
	}
}
