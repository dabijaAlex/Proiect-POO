package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.Card;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CreateCardTransaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class CreateCard extends Command {
    @JsonIgnore
    private HashMap<String, User> users;
    @JsonIgnore
    private String IBAN;

    private int timestamp;
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    public CreateCard(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.cardHolder = command.getEmail();
        this.timestamp = command.getTimestamp();
        super.timestamp = timestamp;

        this.users = users;


        this.description = "New card created";
    }
    public void execute(final ArrayNode output) throws NotFoundException {

        User user = getUserReference(users, account);
        User check = getUserReference(users, cardHolder);
        //  check IBAN and email are from same user
        if(user != check)
            return;
        Account cont = getAccountReference(users, account);

        card = Utils.generateCardNumber();
        cont.addCard(new Card(card, "active"));

        //  add card number to hashMap
        users.put(card, user);


        this.IBAN = cont.getIBAN();
        cont.addTransaction(new CreateCardTransaction(timestamp, description, card, cardHolder, account));
        user.addTransaction(this);
    }




    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }
}
