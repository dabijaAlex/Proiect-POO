package org.poo.app;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Card {
    private String cardNumber;
    private String status;

    public Card(String cardNumber, String status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }

    public Card(Card other){
        this.cardNumber = other.getCardNumber();
        this.status = other.getStatus();
    }
}
