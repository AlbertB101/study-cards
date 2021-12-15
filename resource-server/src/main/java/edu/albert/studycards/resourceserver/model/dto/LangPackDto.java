package edu.albert.studycards.resourceserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackPersistent;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

public record LangPackDto(
        Long id,
        Long accountId,
        @NotEmpty String accountEmail,
        @NotEmpty String lang,
        List<CardDto> cards) implements Serializable {

    public LangPackDto(@JsonProperty("accountEmail") String accountEmail,
                       @JsonProperty("lang") String lang,
                       @JsonProperty("cards") List<CardDto> cards) {
        this(null,
                null,
                accountEmail,
                lang,
                cards);
    }

    public LangPackDto(LangPackPersistent langPackP) {
        this(langPackP.getId(),
                null,
                langPackP.getAccountEmail(),
                langPackP.getLang(),
                CardDto.listFrom(langPackP.getCards()));
    }

    public void clearCards() {
        cards.clear();
    }

    public CardDto getCard(int n) {
        return cards.get(n);
    }

    public CardDto getCard(String word) throws NoSuchElementException {
        return cards.stream()
                .findFirst()
                .filter(card -> card.word().equals(word))
                .orElseThrow(NoSuchElementException::new);
    }

    public CardDto getCard(Long id) {
        return cards.stream().findFirst()
                .filter(card -> card.id().equals(id))
                .get();
    }


    public boolean contains(String word) {
        return cards.stream()
                .anyMatch(card -> card.word().equals(word));
    }

    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        return "LangPackDtoImpl{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", accountEmail='" + accountEmail + '\'' +
                ", lang='" + lang + '\'' +
                ", cards=" + cards +
                '}';
    }
}
