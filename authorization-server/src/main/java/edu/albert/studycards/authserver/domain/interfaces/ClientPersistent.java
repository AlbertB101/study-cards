package edu.albert.studycards.authserver.domain.interfaces;

import java.util.Date;

public interface ClientPersistent {
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
	
	void setAccount(AccountPersistent account);
	AccountPersistent getAccount();
}
