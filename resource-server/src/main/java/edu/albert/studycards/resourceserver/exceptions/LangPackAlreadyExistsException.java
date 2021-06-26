package edu.albert.studycards.resourceserver.exceptions;

public class LangPackAlreadyExistsException extends Exception {
	
	private static final String COMMON_MESSAGE = "Such LangPack already exists in repository";
	
	public LangPackAlreadyExistsException() {
		super(COMMON_MESSAGE);
	}
	
	public LangPackAlreadyExistsException(String message) {
		super(message);
	}
	
	public LangPackAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
