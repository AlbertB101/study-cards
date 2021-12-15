package edu.albert.studycards.resourceserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record CardDto(
        Long id,
        Long langPackId,
        Long accountId,
        @NotBlank String lang,
        @NotBlank String word,
        @NotBlank String wordTr,
        @NotBlank String wordMng

) implements Serializable {

    public CardDto(@JsonProperty("word") String word,
                   @JsonProperty("wordTr") String wordTr,
                   @JsonProperty("wordMng") String wordMng) {
        this(null,
                null,
                null,
                null,
                word,
                wordTr,
                wordMng);
    }

    /**
     * <p>Constructor for creating new Dto entity using {@link CardPersistent}</p>
     *
     * @param card {@link CardPersistent} implementation.
     */
    public CardDto(CardPersistent card) {
        this(card.getId(),
                card.getLangPack().getId(),
                null,
                card.getLangPack().getLang(),
                card.getWord(),
                card.getWordTr(),
                card.getWordMng()
        );
    }

    static List<CardDto> listFrom(List<CardPersistent> givenCards) {
        List<CardDto> result = new ArrayList<>(givenCards.size());
        for (CardPersistent cardP : givenCards) {
            CardDto card = new CardDto(cardP);
            result.add(card);
        }
        return result;
    }

    @Override
    public String toString() {
        return "CardDto{" +
                "id=" + id +
                ", langPackId=" + langPackId +
                ", accountId=" + accountId +
                ", lang='" + lang + '\'' +
                ", word='" + word + '\'' +
                ", wordTr='" + wordTr + '\'' +
                ", wordMng='" + wordMng + '\'' +
                '}';
    }
}
