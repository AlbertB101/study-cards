package edu.albert.studycards.authserver.domain.interfaces;

public interface AccountPersistent {
	Long getId();
	
	Role getRole();
	void setRole(Role role);
	
	Status getStatus();
	void setStatus(Status status);
	
	void setClient(ClientPersistent client);
	ClientPersistent getClient();
}
