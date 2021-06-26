package edu.albert.studycards.resourceserver.model.dto;

import edu.albert.studycards.resourceserver.model.interfaces.CardDto;
import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class CardDtoImpl implements CardDto {
	private Long id;
	private Long langPackId;
	private Long accountId;
	@NotBlank
	private String lang;
	@NotBlank
	private String word;
	@NotBlank
	private String wordTr;
	@NotBlank
	private String wordMng;
	
	public CardDtoImpl(String word, String wordTr, String wordMng) {
		this.word = word;
		this.wordTr = wordTr;
		this.wordMng = wordMng;
	}
	
	public CardDtoImpl(CardDto card) {
		this.id = card.getId();
		this.langPackId = card.getLangPackId();
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
		this.lang = card.getLangPack().getLang();
		this.word = card.getWord();
		this.wordTr = card.getWordTr();
		this.wordMng = card.getWordMng();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CardDtoImpl cardDto = (CardDtoImpl) o;
		return Objects.equals(word, cardDto.word) &&
			       Objects.equals(wordTr, cardDto.wordTr) &&
			       Objects.equals(wordMng, cardDto.wordMng);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(word, wordTr, wordMng);
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
