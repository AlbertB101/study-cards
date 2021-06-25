package edu.albert.studycards.resourceserver.model.interfaces;

public interface LangPack {
	Long getId();
	void setId(Long id);
	
	String getLang();
	void setLang(String lang);
	
	String getAccountEmail();
	void setAccountEmail(String accountEmail);
	
	boolean contains(String word);
}
