package edu.albert.studycards.resourceserver.repository;

import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;
import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardPersistentImpl, Long> {
	Optional<CardPersistent> findByWordAndLangPack_AccountEmail(String word, String accEmail);
	void deleteByWordAndLangPack_LangAndLangPack_AccountEmail(
		String word, String lang, String accEmail);
}
