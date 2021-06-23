package edu.albert.studycards.resourceserver.model.dto;

import edu.albert.studycards.resourceserver.model.interfaces.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	private String accountEmail;
	private String lang;
	private List<CardDto> cards = new ArrayList<>();
	
	public LangPackDtoImpl(String lang) {
		this.lang = lang;
	}
	
	public LangPackDtoImpl(LangPackDto langPackDto) {
		this.id = langPackDto.getId();
		this.accountId = langPackDto.getAccountId();
		this.lang = langPackDto.getLang();
		this.cards = langPackDto.getCards();
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
		if (hasCard(word)) {
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
	public boolean hasCard(String word) {
		return cards.stream()
			       .anyMatch(card -> card.getWord().equals(word));
	}
	
	@Override
	public int size() {
		return cards.size();
	}
}
