package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.NotFoundException;
import org.poo.app.OneTimeCard;
import org.poo.app.User;
import org.poo.app.Account;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CreateCardTransaction;
import org.poo.utils.Utils;

import java.util.HashMap;

@Getter @Setter
public final class CreateOneTimeCard extends Command {
    private HashMap<String, User> users;

    private int timestamp;
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public CreateOneTimeCard(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.cardHolder = command.getEmail();
        this.timestamp = command.getTimestamp();


        this.users = users;

        this.description = "New card created";
    }


    /**
     * for when we need to create a new 1-time after another has been used
     * @param timestamp
     * @param cardHolder
     * @param account
     * @param users
     */
    public CreateOneTimeCard(final int timestamp, final String cardHolder,
                             final String account, final HashMap<String, User> users) {
        this.timestamp = timestamp;
        this.cardHolder = cardHolder;
        this.account = account;
        this.users = users;
        this.description = "New card created";
        this.cmdName = "CreateOneTimeCard";
    }


    /**
     * check if email and Iban are from the same user
     *
     * gen card number
     * add (card number, user) pair to the users hash map
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        User user = getUserReference(users, account);
        Account cont = getAccountReference(users, account);

        String cardNumber = Utils.generateCardNumber();
        cont.addCard(new OneTimeCard(cardNumber, "active"));
        users.put(cardNumber, user);


        cont.addTransaction(new CreateCardTransaction(timestamp, description, cardNumber,
                cardHolder, account));
    }

}
