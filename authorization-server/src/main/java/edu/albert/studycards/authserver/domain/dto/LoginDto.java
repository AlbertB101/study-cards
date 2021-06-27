package edu.albert.studycards.authserver.domain.dto;

/**
 * Interface for DTO object that will be used
 * for logging requests
 */
public interface LoginDto {
	String getEmail();
	void setEmail(String email);
	
	String getPassword();
	void setPassword(String password);
}
