package edu.albert.studycards.authserver.service;


import edu.albert.studycards.authserver.SourceProvider;
import edu.albert.studycards.authserver.domain.interfaces.ClientDto;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;
import edu.albert.studycards.authserver.repository.ClientRepository;
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
	private final static List<ClientDto> ACCOUNTS = SourceProvider.getRandomAccountDto(ACCOUNT_AMOUNT);
	private final static ClientDto CLIENT = ACCOUNTS.get(0);
	
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
	@DisplayName("Should successfully register all clients")
	void shouldSuccessfullyRegisterAllClients() {
		List<Future<?>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(clientRepo.existsByEmail(any(String.class)))
			.thenReturn(false);
		
		for (ClientDto clientDto : ACCOUNTS) {
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
	@DisplayName("Should throw ClientAlreadyExistException for all clients")
	void shouldThrowClientAlreadyExistExceptionForAllClients() {
		List<Future<?>> execFutures = new ArrayList<>(ACCOUNT_AMOUNT);
		
		when(
			clientRepo.existsByEmail(
				any(String.class)
			)
		).thenReturn(true);
		
		for (ClientDto clientDto : ACCOUNTS) {
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
}