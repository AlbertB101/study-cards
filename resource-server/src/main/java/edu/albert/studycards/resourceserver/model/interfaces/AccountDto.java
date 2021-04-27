package edu.albert.studycards.resourceserver.model.interfaces;

public interface AccountDto {
	Long getId();
	void setId(Long id);
	
	String getEmail();
	void setEmail(String email);
	
	String getPassword();
	void setPassword(String);
	
	String getFirstName();
	void setFirstName(String firstName);
	
	String getLastName();
	void setLastName(String lastName);
	
	Role getRole();
	void setRole(Role role);
	
	Status getStatus();
	void setStatus(Status status);
}
