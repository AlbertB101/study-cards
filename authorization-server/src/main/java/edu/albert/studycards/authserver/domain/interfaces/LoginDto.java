package edu.albert.studycards.authserver.domain.interfaces;

public interface LoginDto {
	String getEmail();
	void setEmail(String email);
	
	String getPassword();
	void setPassword(String password);
}
