package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.Card;
import org.poo.app.OneTimeCard;
import org.poo.app.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class CreateOneTimeCard extends Command {
    @JsonIgnore
    private HashMap<String, User> users;
    @JsonIgnore
    private String IBAN;
//    @JsonIgnore
//    private String cmdName;

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
        super.timestamp = timestamp;


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


    public void execute(final ArrayNode output) {
        User user = getUserReference(users, account);
        Account cont = getAccountReference(users, account);

        cardNumber = Utils.generateCardNumber();
        cont.addCard(new OneTimeCard(cardNumber, "active"));
        this.card = cardNumber;
        users.put(cardNumber, user);

        this.IBAN = cont.getIBAN();
        user.addTransaction(this);

    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }

}
