package edu.albert.studycards.authserver.domain.interfaces;

import java.util.Date;

public interface UserAccountPersistent {
	String getFirstName();
	void setFirstName(String firstName);
	
	String getLastName();
	void setLastName(String lastName);
	
	String getEmail();
	void setEmail(String email);
	
	String getPassword();
	void setPassword(String password);
	
	Long getId();
//	void setId(Long id);
	
	Date getCreated();
	
	Role getRole();
	void setRole(Role role);
	
	Status getStatus();
	void setStatus(Status status);
	
	String getToken();
	void setToken(String token);
	
}
