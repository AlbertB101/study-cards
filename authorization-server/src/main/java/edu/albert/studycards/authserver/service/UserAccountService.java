package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.interfaces.UserAccountDto;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public interface UserAccountService {
	
	CompletableFuture<UserAccountDto> registerClient(UserAccountDto userAccountDto) throws ClientAlreadyExistsException;
	
	CompletableFuture<UserAccountDto> receiveClient(String email) throws NoSuchElementException;
	
	CompletableFuture<UserAccountDto> receiveClient(Long id) throws NoSuchElementException;
	
	
	CompletableFuture<UserAccountDto> updateClient(UserAccountDto userAccountDto) throws NoSuchElementException;
	
	CompletableFuture<Void> deleteClient(String email) throws NoSuchElementException;
	
	CompletableFuture<Void> deleteClient(Long id) throws NoSuchElementException;
}
