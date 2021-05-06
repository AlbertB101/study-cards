package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.interfaces.UserAccountPersistent;
import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccountPersistentImpl, Long> {
	@Transactional
	Optional<UserAccountPersistent> findByEmail(@Param("email") String clientEmail);
	
	boolean existsByEmail(String email);
	void deleteByEmail(String email);
	
	@Modifying(clearAutomatically = true)
	@Query("update UserAccount c set c.firstName = :firstName, c.lastName = :lastName where c.email = :email")
	void updateUserAccByEmail(@Param("email") String email,
	                          @Param("firstName") String firsName,
	                          @Param("lastName") String lastName);
}
