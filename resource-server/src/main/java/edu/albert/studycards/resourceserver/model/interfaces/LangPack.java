package edu.albert.studycards.resourceserver.model.interfaces;

import edu.albert.studycards.resourceserver.model.dto.LangPackDtoImpl;

import java.util.List;

public interface LangPack {
	static LangPackDto persistentToDto(LangPackPersistent langPackPersistent) {
		LangPackDto langPackDto = new LangPackDtoImpl();
		langPackDto.setId(langPackPersistent.getId());
		langPackDto.setLang(langPackPersistent.getLang());
		langPackDto.setAccountId(langPackPersistent.getAccount().getId());
		List<CardDto> dtoList = Card.from(langPackPersistent.getCards());
		langPackDto.setCards(dtoList);
		
		return langPackDto;
	}
}
