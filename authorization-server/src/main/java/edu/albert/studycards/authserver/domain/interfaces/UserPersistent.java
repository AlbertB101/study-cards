package edu.albert.studycards.authserver.domain.interfaces;

import java.util.Date;

public interface UserPersistent {
	Long getId();
	
	String getFirstName();
	void setFirstName(String firstName);
	
	String getLastName();
	void setLastName(String lastName);
	
	String getEmail();
	void setEmail(String email);
	
	String getPassword();
	void setPassword(String password);
	
}
