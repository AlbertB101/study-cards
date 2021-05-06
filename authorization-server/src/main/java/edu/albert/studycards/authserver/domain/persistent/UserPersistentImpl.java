package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.interfaces.UserDto;
import edu.albert.studycards.authserver.domain.interfaces.UserPersistent;
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
public class UserPersistentImpl implements UserPersistent, Serializable {
	
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
	
	public UserPersistentImpl(UserDto userDto) {
		this.email = userDto.getEmail();
		this.firstName = userDto.getFirstName();
		this.lastName = userDto.getLastName();
		this.password = userDto.getPassword();
	}
}
