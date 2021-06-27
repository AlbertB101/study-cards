package edu.albert.studycards.authserver.domain.dto;

public interface AccountRegistrationRequest {
	String getFirstName();
	
	String getLastName();
	
	String getEmail();
	
	String getPassword();
	
	void setFirstName(String firstName);
	
	void setLastName(String lastName);
	
	void setEmail(String email);
	
	void setPassword(String password);
}
