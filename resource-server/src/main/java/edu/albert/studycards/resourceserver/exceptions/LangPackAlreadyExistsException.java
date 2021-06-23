package edu.albert.studycards.resourceserver.exceptions;

public class LangPackAlreadyExistsException extends Exception {
	public LangPackAlreadyExistsException() {
	}
	
	public LangPackAlreadyExistsException(String message) {
		super(message);
	}
	
	public LangPackAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
