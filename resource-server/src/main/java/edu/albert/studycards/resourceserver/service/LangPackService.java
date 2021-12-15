package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.exceptions.LangPackAlreadyExistsException;
import edu.albert.studycards.resourceserver.model.dto.LangPackDto;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackPersistent;

public interface LangPackService {
	LangPackDto create(LangPackDto langPackDto) throws LangPackAlreadyExistsException;
	
	LangPackDto get(Long id);
	
	LangPackDto get(String lang);
	
	LangPackDto update(LangPackDto langPackDto);
	
	void delete(Long id);
	
	LangPackPersistent find(Long id);
	
	LangPackPersistent find(String lang);
}
