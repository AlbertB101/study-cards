package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.interfaces.AccountPersistent;
import edu.albert.studycards.authserver.domain.interfaces.UserPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Role;
import edu.albert.studycards.authserver.domain.interfaces.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.Date;

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
		targetEntity = UserPersistentImpl.class
	)
	@JoinColumn(
		name = "CLIENT_EMAIL",
		referencedColumnName = "EMAIL",
		nullable = false
	)
	private UserPersistent client;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "ROLE")
	private Role role;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	private Status status;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date created;
	
	public AccountPersistentImpl(UserPersistentImpl client) {
		this.client = client;
		this.role = Role.USER;
		this.status = Status.ACTIVE;
		this.created = new Date();
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
