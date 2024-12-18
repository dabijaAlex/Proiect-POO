package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
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

    public DeleteCard(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.timestamp = command.getTimestamp();
        super.timestamp = timestamp;
        this.card = command.getCardNumber();
        this.users = users;
        this.description = "The card has been destroyed";
    }

    //  for when we need to delete and recreate a one time use card
    public DeleteCard(int timestamp, String cardNumber, HashMap<String, User> users) {
        this.timestamp = timestamp;
        super.timestamp = timestamp;
        this.card = cardNumber;
        this.users = users;
        this.description = "The card has been destroyed";
        this.cmdName = "deleteCard";
    }


    public void execute(final ArrayNode output) throws NotFoundException {
        User user = getUserReference(users, card);
        Account acc = getAccountReference(users, card);

        cardHolder = user.getEmail();
        account = acc.getIBAN();


        acc.deleteCard(card);
        users.remove(card);

        super.account = acc.getIBAN();
        acc.addTransaction(new DeleteCardTransaction(timestamp, description, card, cardHolder, account));
    }
}
