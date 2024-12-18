package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.*;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CreateCardTransaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class CreateOneTimeCard extends Command {
    private HashMap<String, User> users;

    private int timestamp;
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    public CreateOneTimeCard(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.cardHolder = command.getEmail();
        this.timestamp = command.getTimestamp();


        this.users = users;

        this.description = "New card created";
    }

    //  for when we need to create a new 1-time after another has been used
    public CreateOneTimeCard(int timestamp, String cardHolder, String account, HashMap<String, User> users) {
        this.timestamp = timestamp;
        this.cardHolder = cardHolder;
        this.account = account;
        this.users = users;
        this.description = "New card created";
        this.cmdName = "CreateOneTimeCard";
    }


    public void execute(final ArrayNode output) throws NotFoundException {
        User user = getUserReference(users, account);
        Account cont = getAccountReference(users, account);

        String cardNumber = Utils.generateCardNumber();
        cont.addCard(new OneTimeCard(cardNumber, "active"));
        users.put(cardNumber, user);


        cont.addTransaction(new CreateCardTransaction(timestamp, description, cardNumber, cardHolder, account));
    }

}
