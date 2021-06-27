package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.config.AppConfig;
import edu.albert.studycards.authserver.domain.dto.AccountRegistrationRequest;
import edu.albert.studycards.authserver.domain.dto.UserAccountDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountDto;
import edu.albert.studycards.authserver.domain.interfaces.UserAccountPersistent;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistentImpl;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;
import edu.albert.studycards.authserver.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

/**
 * Service class managing {@link UserAccountPersistent} CRUD operations.
 *
 * <p>In this class I make a try to use asynchronous features of spring boot.
 * Every API method is annotated {@link Async} annotation that uses
 * configured {@link ThreadPoolTaskExecutor} from {@link AppConfig}.
 * Every API method returns {@link CompletableFuture} with return value inside.
 *
 * <p> {@link UserAccountRepository} and {@link PasswordEncoder} seems to be thread because
 * they are managed by spring container
 * <a href=https://stackoverflow.com/questions/15965735/is-a-spring-data-jpa-repository-thread-safe-aka-is-simplejparepository-threa/15971952>
 * (link)</a>. So it seems this class should be thread safe.
 *
 *
 * <p>I'm not sure I use this feature properly, but
 * I've wanted to get first experience with asynchronous computing.
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {
	
	@Autowired
	UserAccountRepository userAccRepo;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserAccountDto> register(AccountRegistrationRequest regRequest) throws ClientAlreadyExistsException {
		if (userAccRepo.existsByEmail(regRequest.getEmail()))
			throw new ClientAlreadyExistsException();

//		TODO: add email validity check
//		TODO: receive from client already encoded password
		
		UserAccountPersistentImpl userAcc = new UserAccountPersistentImpl(regRequest);
		userAcc.setPassword(passwordEncoder.encode(userAcc.getPassword()));
		userAccRepo.saveAndFlush(userAcc);
		
		return CompletableFuture.completedFuture(new UserAccountDtoImpl(userAcc));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserAccountDto> receive(String email) throws NoSuchElementException {
		UserAccountPersistent userAcc = find(email);
		return CompletableFuture.completedFuture(new UserAccountDtoImpl(userAcc));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserAccountDto> receive(Long id) throws NoSuchElementException {
		UserAccountPersistent userAcc = find(id);
		return CompletableFuture.completedFuture(new UserAccountDtoImpl(userAcc));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<UserAccountDto> update(UserAccountDto userAccountDto) throws UsernameNotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!userAccRepo.existsByEmail(auth.getName()))
			throw new UsernameNotFoundException("Such a user account doesn't exist");
		
		userAccRepo.updateUserAccByEmail(
			auth.getName(),
			userAccountDto.getFirstName(),
			userAccountDto.getLastName());
		
		UserAccountPersistent userAcc = find(auth.getName());
		return CompletableFuture.completedFuture(new UserAccountDtoImpl(userAcc));
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<Void> delete(String email) {
		if (!userAccRepo.existsByEmail(email))
			throw new NoSuchElementException();
		
		userAccRepo.deleteByEmail(email);
		return CompletableFuture.completedFuture(null);
		
	}
	
	@Async("threadPoolTaskExecutor")
	@Override
	public CompletableFuture<Void> delete(Long id) {
		if (!userAccRepo.existsById(id))
			throw new NoSuchElementException();
		
		userAccRepo.deleteById(id);
		return CompletableFuture.completedFuture(null);
	}
	
	private UserAccountPersistent find(String email) {
		return userAccRepo
			       .findByEmail(email)
			       .orElseThrow(NoSuchElementException::new);
	}
	
	private UserAccountPersistent find(long id) {
		return userAccRepo
			       .findById(id)
			       .orElseThrow(NoSuchElementException::new);
	}
}
