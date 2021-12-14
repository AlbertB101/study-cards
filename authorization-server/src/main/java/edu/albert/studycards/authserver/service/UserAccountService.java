package edu.albert.studycards.authserver.service;

import edu.albert.studycards.authserver.domain.dto.AccountRegistrationRequestDtoRecord;
import edu.albert.studycards.authserver.domain.dto.UserAccountDto;
import edu.albert.studycards.authserver.exception.ClientAlreadyExistsException;

import java.util.concurrent.CompletableFuture;

public interface UserAccountService {

    CompletableFuture<UserAccountDto> register(AccountRegistrationRequestDtoRecord regRequest) throws ClientAlreadyExistsException;

    CompletableFuture<UserAccountDto> receive(String email);

    CompletableFuture<UserAccountDto> receive(Long id);


    CompletableFuture<UserAccountDto> update(UserAccountDto userAccountDto);

    CompletableFuture<Void> delete(String email);

    CompletableFuture<Void> delete(Long id);

}
