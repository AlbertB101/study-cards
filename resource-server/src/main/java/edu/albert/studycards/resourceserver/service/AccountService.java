package edu.albert.studycards.resourceserver.service;

import edu.albert.studycards.resourceserver.exceptions.AccountAlreadyExistsException;
import edu.albert.studycards.resourceserver.model.persistent.AccountPersistentImpl;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import edu.albert.studycards.resourceserver.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Service
public class AccountService {
	
	@Autowired
	AccountRepository accRepo;
	
	public CompletableFuture<Void> create(AccountDto accountDto) throws AccountAlreadyExistsException {
		
		if (exists(accountDto.getEmail()))
			throw new AccountAlreadyExistsException();
		
		AccountPersistentImpl account = new AccountPersistentImpl(accountDto);
		
		accRepo.saveAndFlush(account);
		return CompletableFuture.completedFuture(null);
	}
	
	public CompletableFuture<AccountDto> getInfo(String email) throws NoSuchElementException {
		AccountPersistent acc = find(email);
		return CompletableFuture.completedFuture(Account.from(acc));
	}
	
	public CompletableFuture<AccountDto> getInfo(Long id) throws NoSuchElementException {
		AccountPersistent acc = find(id);
		return CompletableFuture.completedFuture(Account.from(acc));
	}
	
	public CompletableFuture<Void> update(AccountDto accountDto) throws NoSuchElementException {
		AccountPersistent account = find(accountDto.getEmail());
		account.setEmail(accountDto.getEmail());
		return CompletableFuture.completedFuture(null);
	}
	
	public CompletableFuture<Void> delete(String email) throws RuntimeException {
		accRepo.deleteByEmail(email);
		return CompletableFuture.completedFuture(null);
	}
	
	public void delete(Long id) throws RuntimeException {
		accRepo.deleteById(id);
	}
	
	AccountPersistent find(String email) throws NoSuchElementException, IllegalStateException {
		if (email != null && !email.isEmpty())
			return accRepo.findByEmail(email)
				       .orElseThrow(() -> new NoSuchElementException("Account with this email doesn't exist"));
		else
			throw new IllegalArgumentException("email is incorrect");
	}
	
	AccountPersistent find(Long id) throws NoSuchElementException, IllegalStateException {
		if (id != null && id >= 0)
			return accRepo.findById(id)
				       .orElseThrow(() -> new NoSuchElementException("Account with this id doesn't exist"));
		else
			throw new IllegalArgumentException("id is incorrect");
		
	}
	
	private boolean exists(String email) throws IllegalArgumentException {
		if (email != null && !email.isEmpty())
			return accRepo.existsByEmail(email);
		else
			throw new IllegalArgumentException("Email is incorrect");
	}
}
