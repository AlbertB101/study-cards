package edu.albert.studycards.authserver.domain.dto;

import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Albert Banshchikov
 * @since 2021.12.14
 */
public record AccountRegistrationDto(
        @NotBlank @Size(max = 64) String firstName,
        @NotBlank @Size(max = 64) String lastName,
        @NotBlank @Size(max = 256) @NaturalId(mutable = true) String email,
        @NotBlank String password) implements Serializable {
}
