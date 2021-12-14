package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.SourceProvider;
import edu.albert.studycards.authserver.domain.dto.UserAccountDto;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistentImpl;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;
import edu.albert.studycards.authserver.repository.UserAccountRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@EnableAsync
@Transactional
@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {
	
	private final static int ACCOUNT_AMOUNT = 10;
	private final static List<UserAccountDto> CLIENTS = SourceProvider.getRandomAccountDto(ACCOUNT_AMOUNT);
	private final static UserAccountDto CLIENT = CLIENTS.get(0);
	
	private static ThreadPoolTaskExecutor executor;
	
	@Mock
	UserAccountRepository userAccRepo;
	@InjectMocks
	UserAccountServiceImpl clientService;
	
	@BeforeAll
	static void configure() {
		executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(6);
		executor.setMaxPoolSize(12);
		executor.setQueueCapacity(150);
		executor.setThreadGroupName("SomeThreadPoolXD");
		executor.initialize();
	}
	
	@BeforeEach
	void configureMocks() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Disabled
	@Test
	@DisplayName("Should successfully register new client")
	void shouldSuccessfullyRegisterNewClient() {
		when(userAccRepo.existsByEmail(CLIENT.email()))
			.thenReturn(false);
		when(userAccRepo.saveAndFlush(any()))
			.thenReturn(new UserAccountPersistentImpl(CLIENT));
		
//		assertDoesNotThrow(() -> {
//			UserAccountDto userAccountDto = clientService.register(CLIENT).get();
//			assertEquals(CLIENT.email(), userAccountDto.email());
//			assertEquals(CLIENT.firstName(), userAccountDto.firstName());
//			assertEquals(CLIENT.lastName(), userAccountDto.lastName());
//			assertEquals(Role.USER, clientDto.getRole());
//			assertEquals(Status.ACTIVE, clientDto.getStatus());
//		});
	}
	
	@Disabled
	@Test
	@DisplayName("Should successfully register all the clients")
	void shouldSuccessfullyRegisterAllTheClients() {
		List<Future<CompletableFuture<UserAccountDto>>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(userAccRepo.existsByEmail(any(String.class)))
			.thenReturn(false);
		when(userAccRepo.saveAndFlush(any()))
			.thenReturn(new UserAccountPersistentImpl(CLIENT));
		
//		for (UserAccountDto userAccountDto : CLIENTS) {
//			Future<CompletableFuture<UserAccountDto>> future =
//				executor.submit(() -> clientService.register(userAccountDto));
//			execFutures.add(future);
//		}
		
//		for (Future<CompletableFuture<UserAccountDto>> future : execFutures) {
//			assertDoesNotThrow(() -> {
//				CompletableFuture<UserAccountDto> compFuture = future.get();
//				assertTrue(future.isDone());
//				UserAccountDto userAccountDto = compFuture.get();
//				assertEquals(CLIENT.email(), userAccountDto.email());
//				assertEquals(CLIENT.firstName(), userAccountDto.firstName());
//				assertEquals(CLIENT.lastName(), userAccountDto.lastName());
//				assertEquals(Role.USER, clientDto.getRole());
//				assertEquals(Status.ACTIVE, clientDto.getStatus());
//			});
//		}
	}
	
	@Disabled
	@Test
	@DisplayName("When register should throw ClientAlreadyExistsException")
	void shouldThrowClientAlreadyExistsException() {
		when(userAccRepo.existsByEmail(any()))
			.thenReturn(true);
		
//		assertThrows(ClientAlreadyExistsException.class, () -> clientService.register(CLIENT));
	}
	
	@Disabled
	@Test
	@DisplayName("When register should throw ClientAlreadyExistException for all the clients")
	void shouldThrowClientAlreadyExistExceptionForAllTheClients() {
		List<Future<?>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(
			userAccRepo.existsByEmail(
				any(String.class)
			)
		).thenReturn(true);
		
//		for (UserAccountDto userAccountDto : CLIENTS) {
//			execFutures.add(executor.submit(
//				() -> clientService.register(userAccountDto)));
//		}
		
		for (Future<?> future : execFutures) {
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				assertEquals(ClientAlreadyExistsException.class, e.getCause().getClass());
			}
		}
	}
	
	@Test
	@DisplayName("When receive Client Should throw NoSuchElementException")
	void shouldThrowClientAlreadyExistsExceptionWhenTryToGetClient() {
		assertThrows(
			NoSuchElementException.class,
			() -> clientService.receive(CLIENT.email()));
	}
	
	@Test
	@DisplayName("When receive Client should throw NoSuchElementException")
	void shouldThrowClientAlreadyExistsExceptionWhenTryToGetClients() {
		List<Future<CompletableFuture<?>>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		for (UserAccountDto userAccountDto : CLIENTS) {
			Future<CompletableFuture<?>> futureTask =
				executor.submit(() -> clientService.receive(userAccountDto.email()));
			execFutures.add(futureTask);
		}
		
		for (Future<CompletableFuture<?>> future : execFutures) {
			try {
				CompletableFuture<?> compFuture = future.get();
				assertEquals(UserAccountDto.class, compFuture.get().getClass());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				assertEquals(NoSuchElementException.class, e.getCause().getClass());
			}
		}
	}
	
	@Test
	@DisplayName("Should receive CLIENT")
	void shouldReturnClient() {
		when(userAccRepo.findByEmail(any()))
			.thenReturn(Optional.of(new UserAccountPersistentImpl(CLIENT)));
		
		assertDoesNotThrow(() -> {
			CompletableFuture<UserAccountDto> compFuture = clientService.receive(CLIENT.email());
			UserAccountDto userAccountDto = compFuture.get();
			assertEquals(CLIENT.email(), userAccountDto.email());
			assertEquals(CLIENT.firstName(), userAccountDto.firstName());
			assertEquals(CLIENT.lastName(), userAccountDto.lastName());
//			assertEquals(Role.USER, clientDto.getRole());
//			assertEquals(Status.ACTIVE, clientDto.getStatus());
		});
	}
	
	@Test
	@DisplayName("Should receive CLIENT for requests")
	void shouldReturnClientForRequests() {
		List<Future<CompletableFuture<UserAccountDto>>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(userAccRepo.findByEmail(any()))
			.thenReturn(Optional.of(new UserAccountPersistentImpl(CLIENT)));
		
		for (UserAccountDto userAccountDto : CLIENTS) {
			Future<CompletableFuture<UserAccountDto>> futureTask =
				executor.submit(() -> clientService.receive(userAccountDto.email()));
			execFutures.add(futureTask);
		}
		
		for (Future<CompletableFuture<UserAccountDto>> future : execFutures) {
			assertDoesNotThrow(() -> {
				CompletableFuture<UserAccountDto> completableFuture = future.get();
				UserAccountDto userAccountDto = completableFuture.get();
				assertEquals(CLIENT.email(), userAccountDto.email());
				assertEquals(CLIENT.firstName(), userAccountDto.firstName());
				assertEquals(CLIENT.lastName(), userAccountDto.lastName());
//				assertEquals(Role.USER, clientDto.getRole());
//				assertEquals(Status.ACTIVE, clientDto.getStatus());
			});
		}
	}
	
	@Test
	@DisplayName("Should successfully delete Client")
	void shouldSuccessfullyDeleteClient() {
		when(userAccRepo.existsByEmail(CLIENT.email()))
			.thenReturn(true);
		
		assertDoesNotThrow(() -> clientService.delete(CLIENT.email()));
	}
	
	@Test
	@DisplayName("Should successfully delete Clients")
	void shouldSuccessfullyDeleteClientsWithoutExceptions() {
		List<Future<CompletableFuture<?>>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(userAccRepo.existsByEmail(anyString()))
			.thenReturn(true);
		
		for (UserAccountDto userAccountDto : CLIENTS) {
			Future<CompletableFuture<?>> f =
				executor.submit(() -> clientService.delete(userAccountDto.email()));
			execFutures.add(f);
		}
		
		for (Future<CompletableFuture<?>> future : execFutures) {
			assertDoesNotThrow(() -> future.get());
		}
	}
	
	@Test
	@DisplayName("Should throw NoSuchElementException when try to delete Client")
	void shouldThrowNoSuchElementExceptionWhenTryToDeleteClient() {
		when(userAccRepo.existsByEmail(anyString()))
			.thenReturn(false);
		
		assertThrows(
			NoSuchElementException.class,
			() -> clientService.delete(CLIENT.email()));
	}
	
	@Test
	@DisplayName("Should throw NoSuchElementException when try to delete Clients")
	void shouldThrowNoSuchElementExceptionWhenTryToDeleteClients() {
		List<Future<CompletableFuture<?>>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(userAccRepo.existsByEmail(anyString()))
			.thenReturn(false);
		
		for (UserAccountDto userAccountDto : CLIENTS) {
			Future<CompletableFuture<?>> f =
				executor.submit(() -> clientService.delete(userAccountDto.email()));
			execFutures.add(f);
		}
		
		for (Future<CompletableFuture<?>> future : execFutures) {
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				assertEquals(NoSuchElementException.class, e.getCause().getClass());
			}
		}
	}
}