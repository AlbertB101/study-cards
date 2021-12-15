package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.persistent.AccountPersistent;
import edu.albert.studycards.authserver.domain.persistent.AccountPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<AccountPersistentImpl, Long> {
	@Transactional
	Optional<AccountPersistent> findByEmail(@Param("email") String clientEmail);
	
	boolean existsByEmail(String email);
	void deleteByEmail(String email);
	
	@Modifying(clearAutomatically = true)
	@Query("update UserAccount acc set acc.firstName = :firstName, acc.lastName = :lastName where acc.email = :email")
	void updateUserAccByEmail(@Param("email") String email,
	                          @Param("firstName") String firsName,
	                          @Param("lastName") String lastName);
}
