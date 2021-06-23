package edu.albert.studycards.resourceserver.model.interfaces;

public interface LangPack {
	
	Long getId();
	void setId(Long id);
	
	Long getAccountId();
	void setAccountId(Long accountId);
	
	String getLang();
	void setLang(String lang);
}
