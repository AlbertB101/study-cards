package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.dto.ClientDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.persistent.ClientPersistentImpl;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;
import edu.albert.studycards.authserver.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	ClientRepository clientRepo;
	
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<ClientDto> registerClient(ClientDto clientDto) throws ClientAlreadyExistsException {
		if (clientRepo.existsByEmail(clientDto.getEmail()))
			throw new ClientAlreadyExistsException();
//		TODO: add email validity check
		
		ClientPersistent savedClient = clientRepo.saveAndFlush(new ClientPersistentImpl(clientDto));
		
		return CompletableFuture.completedFuture(new ClientDtoImpl(savedClient));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<ClientDto> receiveClient(String email) throws NoSuchElementException {
		ClientPersistent clientPers = clientRepo
			                              .findByEmail(email)
			                              .orElseThrow(NoSuchElementException::new);
		return CompletableFuture.completedFuture(new ClientDtoImpl(clientPers));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<ClientDto> receiveClient(Long id) throws NoSuchElementException {
		ClientPersistent clientPers = clientRepo
			                              .findById(id)
			                              .orElseThrow(NoSuchElementException::new);
		return CompletableFuture.completedFuture(new ClientDtoImpl(clientPers));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<ClientDto> updateClient(ClientDto clientDto) throws NoSuchElementException {
		if (!clientRepo.existsByEmail(clientDto.getEmail()))
			throw new NoSuchElementException();
		
		clientRepo.updateClientByEmail(
			clientDto.getEmail(),
			clientDto.getFirstName(),
			clientDto.getLastName());
		
		ClientPersistent clientPers = clientRepo
			                              .findByEmail(clientDto.getEmail())
			                              .orElseThrow(() -> new RuntimeException("Client wasn't found after updating"));
		
		return CompletableFuture.completedFuture(new ClientDtoImpl(clientPers));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<Void> deleteClient(String email) throws NoSuchElementException {
		if (!clientRepo.existsByEmail(email))
			throw new NoSuchElementException();
		
		clientRepo.deleteByEmail(email);
		return CompletableFuture.completedFuture(null);
		
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<Void> deleteClient(Long id) throws NoSuchElementException {
		if (!clientRepo.existsById(id))
			throw new NoSuchElementException();
		
		clientRepo.deleteById(id);
		return CompletableFuture.completedFuture(null);
	}
	
}
