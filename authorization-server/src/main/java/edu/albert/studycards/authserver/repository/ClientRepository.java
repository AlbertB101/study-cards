package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.persistent.ClientPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientPersistentImpl, Long> {
	@Transactional
	Optional<ClientPersistent> findByEmail(@Param("email") String email);
	
	boolean existsByEmail(String email);
	void deleteByEmail(String email);
	
	@Modifying(clearAutomatically = true)
	@Query("update Client c set c.firstName = :firstName, c.lastName = :lastName where c.email = :email")
	void updateClientByEmail(@Param("email") String email,
	                         @Param("firstName") String firsName,
	                         @Param("lastName") String lastName);
	
}
