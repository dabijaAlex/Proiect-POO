package org.poo.app;

public class OneTimeCard extends Card {
    public OneTimeCard(String cardNumber, String status) {
        super(cardNumber, status);
        oneTime = true;
    }
    public OneTimeCard(Card other){
        super(other);
    }

    public boolean useCard() {
        status = "blocked";
        return oneTime;
    }
}
