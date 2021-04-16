package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.persistent.ClientPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientPersistentImpl, Long> {
}
