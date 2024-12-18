package org.poo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class Card {
    private String cardNumber;
    protected String status;
    @JsonIgnore
    protected boolean oneTime;

    public Card(final String cardNumber, final String status) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.oneTime = false;
    }

    public Card(final Card other){
        this.cardNumber = other.getCardNumber();
        this.status = other.getStatus();
    }

    public boolean useCard(final Account cont, final HashMap<String, User> users) {
        return oneTime;
    }
}
