package edu.albert.studycards.authserver.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class AccountRegistrationRequestDto implements AccountRegistrationRequest {
	
	@NotBlank
	@Size(max = 64)
	private String firstName;
	
	@NotBlank
	@Size(max = 64)
	private String lastName;
	
	@NotBlank
	@Size(max = 256)
	@NaturalId(mutable = true)
	private String email;
	
	@NotBlank
	private String password;
}
