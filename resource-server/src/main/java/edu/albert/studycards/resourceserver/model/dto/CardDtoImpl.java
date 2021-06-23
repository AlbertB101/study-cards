package edu.albert.studycards.resourceserver.model.dto;

import edu.albert.studycards.resourceserver.model.interfaces.CardDto;
import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CardDtoImpl implements CardDto {
    private Long id;
    private Long langPackId;
    private Long accountId;
    private String lang;
    private String word;
    private String wordTr;
    private String wordMng;
    
    public CardDtoImpl(String word, String wordTr, String wordMng) {
        this.word = word;
        this.wordTr = wordTr;
        this.wordMng = wordMng;
    }
    
    public CardDtoImpl(CardDto card) {
        this.id = card.getId();
        this.id = card.getLangPackId();
        this.accountId = card.getAccountId();
        this.lang = card.getLang();
        this.word = card.getWord();
        this.wordTr = card.getWordTr();
        this.wordMng = card.getWordMng();
    }
    
    /**
     * <p>Constructor for creating new Dto entity using {@link CardPersistent}</p>
     *
     * @param card {@link CardPersistent} implementation.
     */
    public CardDtoImpl(CardPersistent card) {
        this.id = card.getId();
        this.langPackId = card.getLangPack().getId();
        this.accountId = card.getLangPack().getId();
        this.word = card.getWord();
        this.wordTr = card.getWordTr();
        this.wordMng = card.getWordMng();
    }
}