package edu.albert.studycards.resourceserver.exceptions;

public class AccountAlreadyExistsException extends Exception {
	public AccountAlreadyExistsException() {
		super();
	}
	
	public AccountAlreadyExistsException(String message) {
		super(message);
	}
	
	public AccountAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
