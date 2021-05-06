package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.interfaces.UserDto;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public interface ClientService {
	
	CompletableFuture<UserDto> registerClient(UserDto userDto) throws ClientAlreadyExistsException;
	
	CompletableFuture<UserDto> receiveClient(String email) throws NoSuchElementException;
	
	CompletableFuture<UserDto> receiveClient(Long id) throws NoSuchElementException;
	
	
	CompletableFuture<UserDto> updateClient(UserDto userDto) throws NoSuchElementException;
	
	CompletableFuture<Void> deleteClient(String email) throws NoSuchElementException;
	
	CompletableFuture<Void> deleteClient(Long id) throws NoSuchElementException;
}
