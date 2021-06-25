package edu.albert.studycards.resourceserver.model.dto;

import edu.albert.studycards.resourceserver.model.interfaces.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class LangPackDtoImpl implements LangPackDto {
	private Long id;
	private Long accountId;
	@NotEmpty
	private String accountEmail;
	@NotEmpty
	private String lang;
	private List<CardDto> cards = new ArrayList<>();
	
	public LangPackDtoImpl(String lang) {
		this.lang = lang;
	}
	
	public LangPackDtoImpl(LangPackDto langPackDto) {
		this.id = langPackDto.getId();
		this.accountId = langPackDto.getAccountId();
		this.accountEmail = langPackDto.getAccountEmail();
		this.lang = langPackDto.getLang();
		this.cards = langPackDto.getCards();
	}
	
	public LangPackDtoImpl(LangPackPersistent langPackP) {
		this.id = langPackP.getId();
		this.accountEmail = langPackP.getAccountEmail();
		this.lang = langPackP.getLang();
		this.cards = CardDto.listFrom(langPackP.getCards());
	}
	
	@Override
	public void addCard(CardDto card) {
		Objects.requireNonNull(card);
		cards.add(card);
	}
	
	@Override
	public void setCard(CardDto givenCard) throws NoSuchElementException {
		CardDto card = getCard(givenCard.getWord());
		cards.set(indexOf(card), givenCard);
	}
	
	@Override
	public void deleteCard(int n) {
		if (n >= 0 && n < cards.size())
			cards.remove(n);
	}
	
	@Override
	public void deleteCard(String word) {
		if (contains(word)) {
			CardDto card = getCard(word);
			int cardIndex = indexOf(card);
			cards.remove(cardIndex);
		}
	}
	
	@Override
	public void clearCards() {
		cards.clear();
	}
	
	@Override
	public CardDto getCard(int n) {
		return cards.get(n);
	}
	
	@Override
	public CardDto getCard(String word) throws NoSuchElementException {
		return cards.stream()
			       .findFirst()
			       .filter(card -> card.getWord().equals(word))
			       .orElseThrow(NoSuchElementException::new);
	}
	
	@Override
	public CardDto getCard(Long id) {
		return cards.stream().findFirst()
			       .filter(card -> card.getId().equals(id))
			       .get();
	}
	
	@Override
	public int indexOf(CardDto card) {
		return cards.indexOf(card);
	}
	
	@Override
	public boolean contains(String word) {
		return cards.stream()
			       .anyMatch(card -> card.getWord().equals(word));
	}
	
	@Override
	public int size() {
		return cards.size();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LangPackDtoImpl that = (LangPackDtoImpl) o;
		return Objects.equals(accountEmail, that.accountEmail) &&
				Objects.equals(lang, that.lang) &&
				Objects.equals(cards, that.cards);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(accountEmail, lang, cards);
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
