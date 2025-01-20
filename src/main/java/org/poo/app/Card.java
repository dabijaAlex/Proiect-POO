package org.poo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.commands.TransactionCommands.PayOnline;
import org.poo.transactions.PayOnlineTransaction;

import java.util.HashMap;

@Getter @Setter
public class Card {
    private String cardNumber;
    protected String status;
    @JsonIgnore
    protected boolean oneTime;
    @JsonIgnore
    protected String email;

    public Card(final String cardNumber, final String status, final String email) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.oneTime = false;
        this.email = email;
    }

    public Card(final Card other) {
        this.cardNumber = other.getCardNumber();
        this.status = other.getStatus();
        this.email = other.getEmail();
    }

    /**
     * the card is regular so we just need to add the transaction to the account
     * @param cont
     * @param users
     * @return
     */
    public void useCard(final Account cont, final HashMap<String, User> users,
                        final double amount, final ArrayNode output, final PayOnline command) {

        cont.addTransaction(new PayOnlineTransaction(command.getTimestamp(),
                command.getDescription(), amount, command.getCommerciantIdentifier()));
    }
}
