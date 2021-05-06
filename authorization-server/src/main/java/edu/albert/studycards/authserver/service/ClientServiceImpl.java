package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.dto.UserDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.UserDto;
import edu.albert.studycards.authserver.domain.interfaces.UserPersistent;
import edu.albert.studycards.authserver.domain.persistent.AccountPersistentImpl;
import edu.albert.studycards.authserver.domain.persistent.UserPersistentImpl;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;
import edu.albert.studycards.authserver.repository.AccountRepository;
import edu.albert.studycards.authserver.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	ClientRepository clientRepo;
	@Autowired
	AccountRepository accountRepo;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserDto> registerClient(UserDto userDto) throws ClientAlreadyExistsException {
		if (clientRepo.existsByEmail(userDto.getEmail()))
			throw new ClientAlreadyExistsException();

//		TODO: add email validity check
//		TODO: refactor creation new Client and Account entities
//		TODO: receive from client already encoded password
		
		
		UserPersistentImpl client = new UserPersistentImpl(userDto);
		client.setPassword(passwordEncoder.encode(client.getPassword()));
		
		AccountPersistentImpl account = new AccountPersistentImpl(client);
		
		clientRepo.saveAndFlush(client);
		accountRepo.saveAndFlush(account);
		
		userDto.setPassword(null);
		return CompletableFuture.completedFuture(userDto);
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserDto> receiveClient(String email) throws NoSuchElementException {
		UserPersistent clientPers = clientRepo
			                              .findByEmail(email)
			                              .orElseThrow(NoSuchElementException::new);
		return CompletableFuture.completedFuture(new UserDtoImpl(clientPers));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserDto> receiveClient(Long id) throws NoSuchElementException {
		UserPersistent clientPers = clientRepo
			                              .findById(id)
			                              .orElseThrow(NoSuchElementException::new);
		return CompletableFuture.completedFuture(new UserDtoImpl(clientPers));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserDto> updateClient(UserDto userDto) throws NoSuchElementException {
		if (!clientRepo.existsByEmail(userDto.getEmail()))
			throw new NoSuchElementException();
		
		clientRepo.updateClientByEmail(
			userDto.getEmail(),
			userDto.getFirstName(),
			userDto.getLastName());
		
		UserPersistent clientPers = clientRepo
			                              .findByEmail(userDto.getEmail())
			                              .orElseThrow(() -> new RuntimeException("Client wasn't found after updating"));
		
		return CompletableFuture.completedFuture(new UserDtoImpl(clientPers));
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
