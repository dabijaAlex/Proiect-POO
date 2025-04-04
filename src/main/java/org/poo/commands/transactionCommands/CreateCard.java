package org.poo.commands.transactionCommands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.notFoundExceptions.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CreateCardTransaction;

import java.util.HashMap;

@Getter @Setter
public final class CreateCard extends Command {
    private HashMap<String, User> users;

    private int timestamp;
    private String description;
    private String card;
    private String cardHolder;
    private String account;

    @JsonIgnore
    private String email;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public CreateCard(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.account = command.getAccount();
        this.cardHolder = command.getEmail();
        this.timestamp = command.getTimestamp();
        this.email = command.getEmail();

        this.users = users;

        this.description = "New card created";
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
        User check = getUserReference(users, cardHolder);
        //  check IBAN and email are from same user
        if (user != check) {
            return;
        }
        Account cont = getAccountReference(users, account);


        card = cont.createCard(email);

        //  add card number to hashMap
        users.put(card, user);

        cont.addTransaction(new CreateCardTransaction(timestamp, description, card, cardHolder,
                account));
    }





}
