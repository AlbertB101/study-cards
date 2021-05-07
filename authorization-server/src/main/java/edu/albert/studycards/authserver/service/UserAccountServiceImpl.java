package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.dto.UserAccountDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountDto;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountPersistent;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistentImpl;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;
import edu.albert.studycards.authserver.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	
	@Autowired
	UserAccountRepository userAccRepo;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserAccountDto> register(UserAccountDto userAccDto) throws ClientAlreadyExistsException {
		if (userAccRepo.existsByEmail(userAccDto.getEmail()))
			throw new ClientAlreadyExistsException();

//		TODO: add email validity check
//		TODO: receive from client already encoded password
		
		UserAccountPersistentImpl userAcc = new UserAccountPersistentImpl(userAccDto);
		userAcc.setPassword(passwordEncoder.encode(userAcc.getPassword()));
		userAccRepo.saveAndFlush(userAcc);
		
		userAccDto.setPassword(null);
		return CompletableFuture.completedFuture(userAccDto);
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserAccountDto> receive(String email) throws NoSuchElementException {
		UserAccountPersistent userAcc = userAccRepo
			                                .findByEmail(email)
			                                .orElseThrow(NoSuchElementException::new);
		return CompletableFuture.completedFuture(new UserAccountDtoImpl(userAcc));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserAccountDto> receive(Long id) throws NoSuchElementException {
		UserAccountPersistent userAcc = userAccRepo
			                                .findById(id)
			                                .orElseThrow(NoSuchElementException::new);
		return CompletableFuture.completedFuture(new UserAccountDtoImpl(userAcc));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserAccountDto> update(UserAccountDto userAccountDto) throws NoSuchElementException {
		if (!userAccRepo.existsByEmail(userAccountDto.getEmail()))
			throw new NoSuchElementException();
		
		userAccRepo.updateUserAccByEmail(
			userAccountDto.getEmail(),
			userAccountDto.getFirstName(),
			userAccountDto.getLastName());
		
		UserAccountPersistent userAcc = userAccRepo
			                                .findByEmail(userAccountDto.getEmail())
			                                .orElseThrow(() -> new RuntimeException("Client wasn't found after updating"));
		
		return CompletableFuture.completedFuture(new UserAccountDtoImpl(userAcc));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<Void> delete(String email) throws NoSuchElementException {
		if (!userAccRepo.existsByEmail(email))
			throw new NoSuchElementException();
		
		userAccRepo.deleteByEmail(email);
		return CompletableFuture.completedFuture(null);
		
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<Void> delete(Long id) throws NoSuchElementException {
		if (!userAccRepo.existsById(id))
			throw new NoSuchElementException();
		
		userAccRepo.deleteById(id);
		return CompletableFuture.completedFuture(null);
	}
	
}
