package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.DeleteCardTransaction;

import java.util.HashMap;

@Getter @Setter
public class DeleteCard extends Command {
    private int timestamp;
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    private HashMap<String, User> users;
    private String email;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public DeleteCard(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.card = command.getCardNumber();
        this.users = users;
        this.description = "The card has been destroyed";
        this.email = command.getEmail();
    }

    /**
     * Constructor for when we need to delete and recreate a one time use card
     * @param timestamp
     * @param cardNumber
     * @param users
     */
    public DeleteCard(final int timestamp, final String cardNumber,
                      final HashMap<String, User> users, final String email) {
        this.timestamp = timestamp;
        this.card = cardNumber;
        this.users = users;
        this.description = "The card has been destroyed";
        this.cmdName = "deleteCard";
        this.email = email;
    }


    /**
     * get account and user references (if null it throws error)
     *
     * delete card from account
     * delete (cardNumber user) pair from user hashmap
     *
     * add this transaction to account
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        User user = getUserReference(users, card);
        Account acc = getAccountReference(users, card);

        cardHolder = user.getEmail();
        if (!cardHolder.equals(email)) {
            return;
        }

        account = acc.getIban();
        acc.deleteCard(card, email);
        users.remove(card);

        acc.addTransaction(new DeleteCardTransaction(timestamp, description, card,
                cardHolder, account));
    }

    /**
     * used for deleting the used one time card
     * @param output
     * @param type
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output, final int type) throws NotFoundException {
        User user = getUserReference(users, card);
        Account acc = getAccountReference(users, card);

        cardHolder = user.getEmail();
        if (!cardHolder.equals(email)) {
            return;
        }
        account = acc.getIban();


        acc.deleteCardOneTime(card, email);
        users.remove(card);

        acc.addTransaction(new DeleteCardTransaction(timestamp, description, card,
                cardHolder, account));
    }
}
