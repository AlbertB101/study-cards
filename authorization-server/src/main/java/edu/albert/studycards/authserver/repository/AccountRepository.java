package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.persistent.AccountPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountPersistentImpl, Long> {
}
