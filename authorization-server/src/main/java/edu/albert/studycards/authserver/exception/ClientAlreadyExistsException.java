package edu.albert.studycards.authserver.exception;

public class ClientAlreadyExistsException extends Exception{
	public ClientAlreadyExistsException() {
		super();
	}
	
	public ClientAlreadyExistsException(String message) {
		super(message);
	}
	
	public ClientAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
