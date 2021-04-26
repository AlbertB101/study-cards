package edu.albert.studycards.authserver.repository;

import edu.albert.studycards.authserver.domain.persistent.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    boolean existsByToken(String token);
    @Transactional
    @Modifying
    @Query(value = "delete from JwtBlacklist as j where current_timestamp > j.expired ")
    void deleteAllExpired();
}