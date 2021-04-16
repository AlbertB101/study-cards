package edu.albert.studycards.authserver.domain.interfaces;

import edu.albert.studycards.authorizationserver.model.type.Role;
import edu.albert.studycards.authorizationserver.model.type.Status;

public interface ClientDto {
	
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
