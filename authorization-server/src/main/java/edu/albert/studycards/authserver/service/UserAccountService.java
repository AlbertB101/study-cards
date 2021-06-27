package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.dto.AccountRegistrationRequest;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountDto;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public interface UserAccountService {
	
	CompletableFuture<UserAccountDto> register(AccountRegistrationRequest regRequest) throws ClientAlreadyExistsException;
	
	CompletableFuture<UserAccountDto> receive(String email) throws NoSuchElementException;
	
	CompletableFuture<UserAccountDto> receive(Long id) throws NoSuchElementException;
	
	
	CompletableFuture<UserAccountDto> update(UserAccountDto userAccountDto) throws NoSuchElementException;
	
	CompletableFuture<Void> delete(String email) throws NoSuchElementException;
	
	CompletableFuture<Void> delete(Long id) throws NoSuchElementException;
}
