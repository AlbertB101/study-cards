package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.interfaces.AccountPersistent;
import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.*;

@Entity(name = "Account")
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
public class AccountPersistentImpl implements AccountPersistent, Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(
		fetch = FetchType.LAZY,
		targetEntity = ClientPersistentImpl.class
	)
	@JoinColumn(
		name = "CLIENT_EMAIL",
		referencedColumnName = "EMAIL",
		nullable = false
	)
	private ClientPersistent client;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "ROLE")
	private Role role;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	private Status status;
	
	public AccountPersistentImpl(ClientPersistentImpl client) {
		this.client = client;
		this.role = Role.USER;
		this.status = Status.ACTIVE;
	}
	
	@Override
	public String toString() {
		return "Account{" +
			       "id=" + id +
			       ", clientId=" + client.getId() +
				   ", clientEmail=" + client.getEmail() +
			       ", role=" + role +
			       ", status=" + status +
			       '}';
	}
}
