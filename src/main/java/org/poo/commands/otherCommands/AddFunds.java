package org.poo.commands.otherCommands;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;

import org.poo.app.accounts.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import java.util.HashMap;


@Getter @Setter
public final class AddFunds extends Command {
    private HashMap<String, User> users;
    private String iban;
    private double amount;
    @JsonIgnore
    private String email;
    private int timestamp;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public AddFunds(final CommandInput command, final HashMap<String, User> users) {
        this.iban = command.getAccount();
        this.amount = command.getAmount();
        this.users = users;
        this.email = command.getEmail();
        this.timestamp = command.getTimestamp();

    }

    /**
     * add teh funds to the account if it exists else throw exception
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        Account cont = getAccountReference(users, iban);
        cont.addFunds(amount, email, timestamp);
    }
}





