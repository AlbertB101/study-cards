package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.interfaces.AccountPersistent;
import edu.albert.studycards.authserver.domain.persistent.AccountPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountPersistentImpl, Long> {
	@Transactional
	Optional<AccountPersistent> findByClient_Email(@Param("email") String clientEmail);
	
}
