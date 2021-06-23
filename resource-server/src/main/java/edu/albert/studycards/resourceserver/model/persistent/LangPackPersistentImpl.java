package edu.albert.studycards.resourceserver.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.albert.studycards.resourceserver.model.interfaces.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "LangPack")
@Table(name = "lang_pack")
public class LangPackPersistentImpl implements LangPackPersistent, Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "acc_id")
	private String accountEmail;
	
	@NotNull
	@Column(name = "lang")
	private String lang;
	
	@OneToMany(
		mappedBy = "langPack",
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		targetEntity = CardPersistentImpl.class)
	@JsonIgnoreProperties(value = "langPack")
	private List<CardPersistent> cards = new ArrayList<>();
	
	public LangPackPersistentImpl(LangPackDto langPackDto) {
        LangPackPersistent langPackP = new LangPackPersistentImpl();
        langPackP.setLang(langPackDto.getLang());
        langPackP.setAccountEmail(langPackDto.getAccountEmail());
        langPackP.setCards(CardPersistent.listFrom(langPackDto.getCards(), this));
    }
	
	public LangPackPersistentImpl(String lang) {
		this.lang = lang;
	}
	
	@Override
	public void addCard(CardPersistent card) {
		Objects.requireNonNull(card);
		cards.add(card);
	}
	
	@Override
	public void setCard(CardPersistent givenCard) throws NoSuchElementException {
		CardPersistent card = getCard(givenCard.getWord());
		cards.set(indexOf(card), givenCard);
		
	}
    
    @Override
    public void editCard(CardDto cardDto) {
        CardPersistent card;
        try {
            card = getCard(cardDto.getWord());
            card.setWordTr(cardDto.getWordTr());
            card.setWordMng(cardDto.getWordMng());
        } catch (NoSuchElementException nsee) {
            System.out.println(cardDto.getWord() + " doesn't exist in repository");
        }
    }
	
	@Override
	public CardPersistent getCard(int n) {
		return cards.get(n);
	}
	
	@Override
	public CardPersistent getCard(String word) throws NoSuchElementException {
		return cards.stream()
			       .filter(card -> word.equals(card.getWord()))
			       .findAny()
			       .orElseThrow(NoSuchElementException::new);
	}
	
	@Override
	public CardPersistent getCard(Long id) throws NoSuchElementException {
		return cards.stream()
			       .filter(card -> card.getId().equals(id))
			       .findFirst()
			       .orElseThrow(NoSuchElementException::new);
	}
	
	@Override
	public int indexOf(CardPersistent card) {
		return cards.indexOf(card);
	}
	
	@Override
	public void deleteCard(int n) {
		if (n >= 0 && n < cards.size())
			cards.remove(n);
	}
	
	@Override
	public void deleteCard(String word) {
		if (hasCard(word)) {
			CardPersistent card = getCard(word);
			int cardIndex = indexOf(card);
			cards.remove(cardIndex);
		}
	}
	
	@Override
	public void clearCards() {
		cards.clear();
	}
	
	@Override
	public boolean hasCard(String word) {
		return false;
	}
	
	@Override
	public int size() {
		return cards.size();
	}
	
	@Override
	public String toString() {
		return "Main lang: " + lang
			       + " ; Size of cardList: " + cards.size()
			       + "\n" + cards.toString();
	}
}