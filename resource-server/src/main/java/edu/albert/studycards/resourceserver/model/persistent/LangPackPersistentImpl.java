package edu.albert.studycards.resourceserver.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.albert.studycards.resourceserver.model.dto.CardDto;
import edu.albert.studycards.resourceserver.model.dto.LangPackDto;
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

@Entity(name = "LangPack")
@Table(name = "lang_pack")
public class LangPackPersistentImpl implements LangPackPersistent, Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "acc_email")
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
		this.lang = Objects.requireNonNull(langPackDto.lang());
		this.accountEmail = Objects.requireNonNull(langPackDto.accountEmail());
		this.cards = CardPersistent.listFrom(Objects.requireNonNull(langPackDto.cards()), this);
	}
	
	public LangPackPersistentImpl(String lang) {
		this.lang = Objects.requireNonNull(lang);
	}

	public LangPackPersistentImpl() {

	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getAccountEmail() {
		return accountEmail;
	}

	@Override
	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	@Override
	public String getLang() {
		return lang;
	}

	@Override
	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	public List<CardPersistent> getCards() {
		return cards;
	}

	@Override
	public void setCards(List<CardPersistent> cards) {
		this.cards = cards;
	}

	@Override
	public void addCard(CardPersistent card) {
		Objects.requireNonNull(card);
		cards.add(card);
	}
	
	@Override
	public void setCard(CardPersistent givenCard) {
		Objects.requireNonNull(givenCard);
		CardPersistent card = getCard(givenCard.getWord());
		cards.set(indexOf(card), givenCard);
		
	}
	
	public void editCard(CardDto cardDto) {
		Objects.requireNonNull(cardDto);
		CardPersistent card;
		try {
			card = getCard(cardDto.word());
			card.setWordTr(cardDto.wordTr());
			card.setWordMng(cardDto.wordMng());
		} catch (NoSuchElementException nsee) {
			System.out.println(cardDto.word() + " doesn't exist in repository");
		}
	}

	@Override
	public CardPersistent getCard(int n) {
		return cards.get(n);
	}
	
	@Override
	public CardPersistent getCard(String word) {
		Objects.requireNonNull(word);
		return cards.stream()
			       .filter(card -> word.equals(card.getWord()))
			       .findAny()
			       .orElseThrow(NoSuchElementException::new);
	}
	
	@Override
	public CardPersistent getCard(Long id) {
		Objects.requireNonNull(id);
		return cards.stream()
			       .filter(card -> card.getId().equals(id))
			       .findFirst()
			       .orElseThrow(NoSuchElementException::new);
	}
	
	@Override
	public int indexOf(CardPersistent card) {
		Objects.requireNonNull(card);
		return cards.indexOf(card);
	}
	
	@Override
	public void deleteCard(int n) {
		if (n >= 0 && n < cards.size())
			cards.remove(n);
	}
	
	@Override
	public void deleteCard(String word) {
		Objects.requireNonNull(word);
		if (contains(word)) {
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
	public boolean contains(String word) {
		Objects.requireNonNull(word);
		return cards.stream()
			       .anyMatch(card -> card.getWord().equals(word));
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