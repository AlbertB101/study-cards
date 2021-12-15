package edu.albert.studycards.resourceserver.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.albert.studycards.resourceserver.model.dto.CardDto;
import edu.albert.studycards.resourceserver.model.interfaces.CardPersistent;
import edu.albert.studycards.resourceserver.model.interfaces.LangPackPersistent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Target;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Card")
@Table(name = "card")
public class CardPersistentImpl implements CardPersistent, Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NaturalId
	private Long id;
	
	@NotNull
	@Size(max = 65)
	@Column(name = "word")
	@NaturalId
	private String word;
	
	@NotNull
	@Size(max = 65)
	@Column(name = "word_transcription")
	private String wordTr;
	
	@NotNull
	@Size(max = 65)
	@Column(name = "word_meaning")
	private String wordMng;
	
	@ManyToOne
	@Target(LangPackPersistentImpl.class)
	@JsonIgnoreProperties(value = "cards")
	private LangPackPersistent langPack;
	
	/**
	 * <p>Constructor for creating new Persistent entity using {@link CardDto} and
	 * {@link LangPackPersistent} which
	 * will have this card</p>
	 *
	 * @param card     {@link CardDto} implementation
	 * @param langPack {@link LangPackPersistent} implementation that has
	 */
	//TODO: refactor creating new Persistent cards
	public CardPersistentImpl(CardDto card, LangPackPersistent langPack) {
		this.word = card.word();
		this.wordTr = card.wordTr();
		this.wordMng = card.wordMng();
		this.langPack = langPack;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CardPersistentImpl that = (CardPersistentImpl) o;
		return Objects.equals(id, that.id) &&
			       Objects.equals(word, that.word) &&
			       Objects.equals(wordTr, that.wordTr) &&
			       Objects.equals(wordMng, that.wordMng) &&
			       Objects.equals(langPack, that.langPack);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, word, wordTr, wordMng, langPack);
	}
	
	@Override
	public String toString() {
		return "CardPersistentImpl{" +
			       "id=" + id +
			       ", word='" + word + '\'' +
			       ", wordTr='" + wordTr + '\'' +
			       ", wordMng='" + wordMng + '\'' +
			       '}';
	}
}