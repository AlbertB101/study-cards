package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.persistent.ClientPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientPersistentImpl, Long> {
	
	Optional<ClientPersistent> findByEmail(String email);
	
}
