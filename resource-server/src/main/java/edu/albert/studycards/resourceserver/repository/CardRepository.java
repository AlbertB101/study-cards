package edu.albert.studycards.resourceserver.repository;

import edu.albert.studycards.resourceserver.model.persistent.CardPersistentImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardPersistentImpl, Long> {
}
