package edu.albert.studycards.resourceserver.repository;

import edu.albert.studycards.resourceserver.model.interfaces.LangPackPersistent;
import edu.albert.studycards.resourceserver.model.persistent.LangPackPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface LangPackRepository extends JpaRepository<LangPackPersistentImpl, Long> {
	boolean existsByAccountIdAndLang(Long accountId, String lang);
	
	Optional<LangPackPersistent> findByAccountIdAndLang(@Param("account_id") Long account_id,
	                                                    @Param("lang") String lang);
	
	Optional<LangPackPersistent> findByAccountEmailAndLang(@Param("account_email") String accountEmail,
	                                                       @Param("lang") String lang);
	
	boolean existsByAccountEmailAndLang(@Param("account_email") String accountEmail,
	                                    @Param("lang") String lang);
	
	@Query("select lp.lang from LangPack lp where lp.accountId = :account_id")
	Optional<List<String>> getAllLanguages(@Param("account_id") Long accountId);
	
	@Transactional
	@Modifying
	@Query("update LangPack lp set lp.lang = :lang where lp.id = :id")
	void updateLangPackLanguage(@Param(value = "id") Long langPackId,
	                            @Param("lang") String lang);
}
