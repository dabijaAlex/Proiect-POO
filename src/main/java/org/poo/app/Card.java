package org.poo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Card {
    private String cardNumber;
    protected String status;
    @JsonIgnore
    protected boolean oneTime;

    public Card(String cardNumber, String status) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.oneTime = false;
    }

    public Card(Card other){
        this.cardNumber = other.getCardNumber();
        this.status = other.getStatus();
    }

    public boolean useCard() {
        return oneTime;
    }
}
