package edu.albert.studycards.authserver.domain.interfaces;

public interface UserAccountDto {
	
	String getFirstName();
	void setFirstName(String firstName);
	
	String getLastName();
	void setLastName(String lastName);
	
	String getEmail();
	void setEmail(String email);
	
	String getPassword();
	void setPassword(String password);
	
	Role getRole();
	void setRole(Role role);
	
	Status getStatus();
	void setStatus(Status status);
}
