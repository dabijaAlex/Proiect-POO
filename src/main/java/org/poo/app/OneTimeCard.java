package org.poo.app;

import org.poo.commands.CreateOneTimeCard;
import org.poo.utils.Utils;

import java.util.HashMap;



public class OneTimeCard extends Card {
    public OneTimeCard(String cardNumber, String status) {
        super(cardNumber, status);
        oneTime = true;
    }
    public OneTimeCard(Card other){
        super(other);
    }

    public boolean useCard(Account account, HashMap<String, User> users) {
//        User user = users.get(account.getIBAN());
//
//        account.deleteCard(this.getCardNumber());
//        String cardNumber = Utils.generateCardNumber();
//
//
//        OneTimeCard oneTimeCard = new OneTimeCard(cardNumber, "active");
//        account.getCards().add(oneTimeCard);
//
//        users.remove(this.getCardNumber());
//        users.put(cardNumber, user);

        return oneTime;

    }
}
