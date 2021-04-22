package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

public interface ClientService {
	
	CompletableFuture<Void> registerClient(ClientDto clientDto) throws ClientAlreadyExistsException;
	
	CompletableFuture<ClientDto> getClient(String email) throws ClientAlreadyExistsException;
	
	CompletableFuture<ClientDto> getClient(Long id) throws ClientAlreadyExistsException;
	
	
	CompletableFuture<ClientDto> updateClient(ClientDto clientDto) throws NoSuchElementException;
	
	CompletableFuture<Void> deleteClient(String email) throws NoSuchElementException;
	
	CompletableFuture<Void> deleteClient(Long id) throws NoSuchElementException;
}
