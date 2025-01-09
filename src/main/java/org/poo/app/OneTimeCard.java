package org.poo.app;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.app.accounts.Account;
import org.poo.commands.TransactionCommands.CreateOneTimeCard;
import org.poo.commands.TransactionCommands.DeleteCard;
import org.poo.commands.TransactionCommands.PayOnline;
import org.poo.transactions.PayOnlineTransaction;

import java.util.HashMap;



public class OneTimeCard extends Card {
    /**
     * Constructor that calls cons of superclass
     * @param cardNumber
     * @param status
     */
    public OneTimeCard(final String cardNumber, final String status) {
        super(cardNumber, status);
        oneTime = true;
    }

    /**
     * copy card for another arrayoutput reference
     * @param other
     */
    public OneTimeCard(final Card other) {
        super(other);
    }

    /**
     * card is one time so we need to delete it and generate another one
     * we also need to add the transaction
     * @param account
     * @param users
     * @param command
     * @param output
     */
    public void useCard(final Account account, final HashMap<String, User> users,
                        final double amount, final ArrayNode output, final PayOnline command) {

        User user = command.getUserReference(users, account.getIBAN());

        account.addTransaction(new PayOnlineTransaction(command.getTimestamp(),
                command.getDescription(), amount, command.getCommerciant()));


        DeleteCard del = new DeleteCard(command.getTimestamp(), command.getCardNumber(),
                users);
        del.execute(output);
        CreateOneTimeCard cr = new CreateOneTimeCard(command.getTimestamp(), user.getEmail(),
                account.getIBAN(), users);
        cr.execute(output);
    }
}
