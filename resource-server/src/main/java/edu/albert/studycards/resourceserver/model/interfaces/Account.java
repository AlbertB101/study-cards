package edu.albert.studycards.resourceserver.model.interfaces;

import edu.albert.studycards.resourceserver.model.dto.AccountDtoImpl;

public interface Account {
	
	static AccountDto from(AccountPersistent account) {
		AccountDtoImpl accountDto = new AccountDtoImpl();
		accountDto.setId(account.getId());
		accountDto.setEmail(account.getEmail());
		accountDto.setNumOfLangs(0);
		return accountDto;
	}
}
