package org.poo.app;

import org.poo.commands.CreateOneTimeCard;
import org.poo.utils.Utils;

import java.util.HashMap;



public class OneTimeCard extends Card {
    public OneTimeCard(final String cardNumber, final String status) {
        super(cardNumber, status);
        oneTime = true;
    }
    public OneTimeCard(final Card other){
        super(other);
    }

    public boolean useCard(final Account account, final HashMap<String, User> users) {
        return oneTime;
    }
}
