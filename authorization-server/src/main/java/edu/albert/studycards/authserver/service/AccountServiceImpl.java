package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.config.AppConfig;
import edu.albert.studycards.authserver.domain.dto.AccountRegistrationDto;
import edu.albert.studycards.authserver.domain.dto.UserAccountDto;
import edu.albert.studycards.authserver.domain.persistent.AccountPersistent;
import edu.albert.studycards.authserver.domain.persistent.AccountPersistentImpl;
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

import static edu.albert.studycards.authserver.config.AppConfig.TASK_EXECUTOR_NAME;

/**
 * Service class managing {@link AccountPersistent} CRUD operations.
 *
 * <p>Every API method is annotated {@link Async} annotation that uses
 * configured {@link ThreadPoolTaskExecutor} from {@link AppConfig}.
 * Every API method returns {@link CompletableFuture} with return value inside.
 *
 * <p> {@link UserAccountRepository} and {@link PasswordEncoder} seems to be thread safe because
 * they are managed by spring container
 * <a href=https://stackoverflow.com/questions/15965735/is-a-spring-data-jpa-repository-thread-safe-aka-is-simplejparepository-threa/15971952>
 * (link)</a>. So it seems this class should be thread safe.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    UserAccountRepository userAccRepo;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Async(TASK_EXECUTOR_NAME)
    @Override
    public CompletableFuture<UserAccountDto> register(AccountRegistrationDto registrationRequest) throws ClientAlreadyExistsException {
        if (userAccRepo.existsByEmail(registrationRequest.email()))
            throw new ClientAlreadyExistsException();

//		TODO: add email validity check
//		TODO: receive from client already encoded password

        AccountPersistentImpl userAcc = new AccountPersistentImpl(registrationRequest);
        userAcc.setPassword(passwordEncoder.encode(userAcc.getPassword()));
        userAccRepo.saveAndFlush(userAcc);

        return CompletableFuture.completedFuture(new UserAccountDto(userAcc));
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<UserAccountDto> receive(String email) throws NoSuchElementException {
        AccountPersistent userAcc = find(email);
        return CompletableFuture.completedFuture(new UserAccountDto(userAcc));
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<UserAccountDto> receive(Long id) throws NoSuchElementException {
        AccountPersistent userAcc = find(id);
        return CompletableFuture.completedFuture(new UserAccountDto(userAcc));
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<UserAccountDto> update(UserAccountDto userAccountDto) throws UsernameNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!userAccRepo.existsByEmail(auth.getName()))
            throw new UsernameNotFoundException("Such a user account doesn't exist");

        userAccRepo.updateUserAccByEmail(
                auth.getName(),
                userAccountDto.firstName(),
                userAccountDto.lastName());

        AccountPersistent userAcc = find(auth.getName());
        return CompletableFuture.completedFuture(new UserAccountDto(userAcc));
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

    private AccountPersistent find(String email) {
        return userAccRepo
                .findByEmail(email)
                .orElseThrow(NoSuchElementException::new);
    }

    private AccountPersistent find(long id) {
        return userAccRepo
                .findById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}
