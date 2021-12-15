package edu.albert.studycards.authserver.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Albert Banshchikov
 * @since 2021.12.14
 */
public record LoginDto(
        @NotBlank @Size(max = 128) String email,
        @NotBlank String password) implements Serializable {
}
