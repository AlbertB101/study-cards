package edu.albert.studycards.authserver.domain.dto;

import edu.albert.studycards.authserver.domain.interfaces.LoginDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class LoginDtoImpl implements LoginDto {
	
	@NotNull
	@NotEmpty
	@Size(max = 128)
	private String email;
	
	@NotNull
	@NotEmpty
	@Size(max = 64)
	private String password;
}
