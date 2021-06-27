package edu.albert.studycards.authserver.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Implementation of {@link AccountRegistrationRequest}.
 */
@NoArgsConstructor
@Getter
@Setter
public class LoginDtoImpl implements LoginDto {
	
	@NotBlank
	@Size(max = 128)
	private String email;
	
	@NotBlank
	private String password;
}
