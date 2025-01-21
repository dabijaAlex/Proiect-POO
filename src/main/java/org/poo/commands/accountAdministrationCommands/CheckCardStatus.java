package org.poo.commands.accountAdministrationCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.notFoundExceptions.CardNotFoundException;
import org.poo.app.accounts.Account;
import org.poo.app.notFoundExceptions.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CardLimitReachedTransaction;

import java.util.HashMap;

@Getter @Setter
public final class CheckCardStatus extends Command {
    private HashMap<String, User> users;
    private int timestamp;
    private String description;
    private String cardNumber;


    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public CheckCardStatus(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.cardNumber = command.getCardNumber();

        this.users = users;
    }

    /**
     *
     * @param output
     */
    public void execute(final ArrayNode output) {
        Account acc = null;
        try {
            acc = getAccountReference(users, cardNumber);
        } catch (NotFoundException e) {
                throw new CardNotFoundException();
        }

        if (acc.getCard(cardNumber).getStatus().equals("frozen")) {
            return;
        }

        if (acc.getBalance() <= acc.getMinBalance()) {
            description = "You have reached the minimum amount of funds, the card will be frozen";
            acc.getCard(cardNumber).setStatus("frozen");

            acc.addTransaction(new CardLimitReachedTransaction(timestamp));

        }
    }
}
