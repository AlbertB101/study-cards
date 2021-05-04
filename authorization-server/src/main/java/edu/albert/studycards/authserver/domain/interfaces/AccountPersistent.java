package edu.albert.studycards.authserver.domain.interfaces;

import java.util.Date;

public interface AccountPersistent {
	Long getId();
	
	void setClient(ClientPersistent client);
	ClientPersistent getClient();
	
	Role getRole();
	void setRole(Role role);
	
	Status getStatus();
	void setStatus(Status status);
	
	Date getCreated();
}
