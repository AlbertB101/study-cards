package edu.albert.studycards.authserver.service;


import edu.albert.studycards.authserver.SourceProvider;
import edu.albert.studycards.authserver.domain.dto.ClientDtoImpl;
import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.domain.persistent.ClientPersistentImpl;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;
import edu.albert.studycards.authserver.repository.ClientRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.ThrowingSupplier;
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
public class ClientServiceTest {
	
	private final static int ACCOUNT_AMOUNT = 10;
	private final static List<ClientDto> CLIENTS = SourceProvider.getRandomAccountDto(ACCOUNT_AMOUNT);
	private final static ClientDto CLIENT = CLIENTS.get(0);
	
	private static ThreadPoolTaskExecutor executor;
	
	@Mock
	ClientRepository clientRepo;
	@InjectMocks
	ClientServiceImpl clientService;
	
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
	
	@Test
	@DisplayName("Should successfully register new client")
	void shouldSuccessfullyRegisterNewClient() {
		when(clientRepo.existsByEmail(CLIENT.getEmail()))
			.thenReturn(false);
		
		assertDoesNotThrow(() -> clientService.registerClient(CLIENT));
	}
	
	@Test
	@DisplayName("Should successfully register all the clients")
	void shouldSuccessfullyRegisterAllTheClients() {
		List<Future<?>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(clientRepo.existsByEmail(any(String.class)))
			.thenReturn(false);
		
		for (ClientDto clientDto : CLIENTS) {
			Future<?> future = executor.submit(() -> clientService.registerClient(clientDto));
			execFutures.add(future);
		}
		
		for (Future<?> future : execFutures) {
			assertDoesNotThrow(() -> {
				future.get();
				assertTrue(future.isDone());
			});
		}
	}
	
	@Test
	@DisplayName("Should throw ClientAlreadyExistsException")
	void shouldThrowClientAlreadyExistsException() {
		when(clientRepo.existsByEmail(any()))
			.thenReturn(true);
		
		assertThrows(ClientAlreadyExistsException.class, () -> clientService.registerClient(CLIENT));
	}
	
	@Test
	@DisplayName("Should throw ClientAlreadyExistException for all the clients")
	void shouldThrowClientAlreadyExistExceptionForAllTheClients() {
		List<Future<?>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(
			clientRepo.existsByEmail(
				any(String.class)
			)
		).thenReturn(true);
		
		for (ClientDto clientDto : CLIENTS) {
			execFutures.add(executor.submit(
				() -> clientService.registerClient(clientDto)));
		}
		
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
	@DisplayName("Should throw ClientAlreadyExistsException when try to get Client")
	void shouldThrowClientAlreadyExistsExceptionWhenTryToGetClient() {
		when(clientRepo.exists(any()))
			.thenReturn(false);
		
		assertThrows(
			ClientAlreadyExistsException.class,
			() -> clientService.getClient(CLIENT.getEmail()));
	}
	
	@Test
	@DisplayName("Should throw ClientAlreadyExistsException when try to get Clients")
	void shouldThrowClientAlreadyExistsExceptionWhenTryToGetClients() {
		List<Future<CompletableFuture<?>>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(clientRepo.exists(any()))
			.thenReturn(false);
		
		for (ClientDto clientDto : CLIENTS) {
			Future<CompletableFuture<?>> futureTask =
				executor.submit(() -> clientService.getClient(clientDto.getEmail()));
			execFutures.add(futureTask);
		}
		
		for (Future<CompletableFuture<?>> future : execFutures) {
			try {
				CompletableFuture<?> compFuture = future.get();
				assertEquals(ClientDto.class, compFuture.get().getClass());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				assertEquals(ClientAlreadyExistsException.class, e.getCause());
			}
		}
	}
	
	@Test
	@DisplayName("Should return CLIENT")
	void shouldReturnClient() {
		when(clientRepo.exists(any()))
			.thenReturn(true);
		when(clientRepo.findByEmail(any()))
			.thenReturn(Optional.of(new ClientPersistentImpl(CLIENT)));
		
		assertDoesNotThrow(() -> {
			CompletableFuture<ClientDto> compFuture = clientService.getClient(CLIENT.getEmail());
			ClientDto clientDto = compFuture.get();
			assertEquals(CLIENT.getEmail(), clientDto.getEmail());
			assertEquals(CLIENT.getFirstName(), clientDto.getFirstName());
			assertEquals(CLIENT.getLastName(), clientDto.getLastName());
			assertEquals(CLIENT.getRole(), clientDto.getRole());
			assertEquals(CLIENT.getStatus(), clientDto.getStatus());
		});
	}
	
	@Test
	@DisplayName("Should return CLIENT for requests")
	void shouldReturnClientForRequests() {
		List<Future<CompletableFuture<ClientDto>>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(clientRepo.exists(any()))
			.thenReturn(true);
		when(clientRepo.findByEmail(any()))
			.thenReturn(Optional.of(new ClientPersistentImpl(CLIENT)));
		
		for (ClientDto clientDto : CLIENTS) {
			Future<CompletableFuture<ClientDto>> futureTask =
				executor.submit(() -> clientService.getClient(clientDto.getEmail()));
			execFutures.add(futureTask);
		}
		
		for (Future<CompletableFuture<ClientDto>> future : execFutures) {
			assertDoesNotThrow(() -> {
				CompletableFuture<ClientDto> completableFuture = future.get();
				ClientDto clientDto = completableFuture.get();
				assertEquals(CLIENT.getEmail(), clientDto.getEmail());
				assertEquals(CLIENT.getFirstName(), clientDto.getFirstName());
				assertEquals(CLIENT.getLastName(), clientDto.getLastName());
				assertEquals(CLIENT.getRole(), clientDto.getRole());
				assertEquals(CLIENT.getStatus(), clientDto.getStatus());
			});
		}
	}
	
	@Test
	@DisplayName("Should successfully delete Client")
	void shouldSuccessfullyDeleteClient() {
		when(clientRepo.existsByEmail(CLIENT.getEmail()))
			.thenReturn(true);
		
		assertDoesNotThrow(() -> clientService.deleteClient(CLIENT.getEmail()));
	}
	
	@Test
	@DisplayName("Should successfully delete Clients")
	void shouldSuccessfullyDeleteClientsWithoutExceptions() {
		List<Future<CompletableFuture<?>>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(clientRepo.existsByEmail(CLIENT.getEmail()))
			.thenReturn(true);
		
		for (ClientDto clientDto : CLIENTS) {
			Future<CompletableFuture<?>> f =
				executor.submit(() -> clientService.deleteClient(clientDto.getEmail()));
			execFutures.add(f);
		}
		
		for (Future<CompletableFuture<?>> future : execFutures) {
			assertDoesNotThrow(() -> future.get());
		}
	}
	
	@Test
	@DisplayName("Should throw NoSuchElementException when try to delete Client")
	void shouldThrowNoSuchElementExceptionWhenTryToDeleteClient() {
		when(clientRepo.existsByEmail(CLIENT.getEmail()))
			.thenReturn(false);
		
		assertThrows(
			NoSuchElementException.class,
			() -> clientService.deleteClient(CLIENT.getEmail()));
	}
	
	@Test
	@DisplayName("Should throw NoSuchElementException when try to delete Clients")
	void shouldThrowNoSuchElementExceptionWhenTryToDeleteClients() {
		List<Future<CompletableFuture<?>>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(clientRepo.existsByEmail(CLIENT.getEmail()))
			.thenReturn(false);
		
		for (ClientDto clientDto : CLIENTS) {
			Future<CompletableFuture<?>> f =
				executor.submit(() -> clientService.deleteClient(clientDto.getEmail()));
			execFutures.add(f);
		}
		
		for (Future<CompletableFuture<?>> future : execFutures) {
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				assertEquals(NoSuchElementException.class, e.getCause());
			}
		}
	}
}