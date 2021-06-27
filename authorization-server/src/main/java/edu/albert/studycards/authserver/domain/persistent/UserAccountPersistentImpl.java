package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.dto.AccountRegistrationRequest;
import edu.albert.studycards.authserver.domain.Role;
import edu.albert.studycards.authserver.domain.Status;
import edu.albert.studycards.authserver.domain.dto.UserAccountDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "UserAccount")
@Table(name = "userAccount")
@Getter
@Setter
@NoArgsConstructor
public class UserAccountPersistentImpl implements UserAccountPersistent, Serializable {
	
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
	
	@Column
	@Size(max = 128)
	private String email;
	
	@NotNull
	@Column
	@Size(max = 64)
	private String password;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date created;
	
	@Column(name = "role")
	@Enumerated(value = EnumType.STRING)
	private Role role;
	
	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private Status status;
	
	@Column(name = "jwt_token")
	private String token;
	
	public UserAccountPersistentImpl(UserAccountDto userAcc) {
		this.email = userAcc.getEmail();
		this.firstName = userAcc.getFirstName();
		this.lastName = userAcc.getLastName();
		this.password = userAcc.getPassword();
		this.created = new Date();
		this.role = Role.USER;
		this.status = Status.ACTIVE;
	}
	
	public UserAccountPersistentImpl(AccountRegistrationRequest regRequest) {
		this.email = regRequest.getEmail();
		this.firstName = regRequest.getFirstName();
		this.lastName = regRequest.getLastName();
		this.password = regRequest.getPassword();
		this.created = new Date();
		this.role = Role.USER;
		this.status = Status.ACTIVE;
	}
}
