package org.poo.app;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Account {
    @JsonProperty("IBAN")
    private String IBAN;
    private double balance;
    private String currency;
    private String type;
    private ArrayList<Card> cards;

    public Account(String IBAN, double balance, String currency, String type) {
        this.IBAN = IBAN;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
    }
    public void addCard(Card card) {
        this.cards.add(card);
    }

    public Account(Account other) {
        this.IBAN = other.IBAN;
        this.balance = other.balance;
        this.currency = other.currency;
        this.type = other.type;
        this.cards = new ArrayList<>();
        for (Card card : other.getCards()) {
            this.cards.add(new Card(card));
        }
    }
}
