package org.poo.commands.otherCommands;

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
public final class SetMinBalance extends Command {
    private HashMap<String, User> users;
    private String iban;
    private double amount;
    private int timestamp;


    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public SetMinBalance(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.iban = command.getAccount();
        this.amount = command.getAmount();
        this.timestamp = command.getTimestamp();

        this.users = users;
    }

    /**
     * if account doesn t exist throw exception else set a minimum balance
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        Account acc = getAccountReference(users, iban);
        acc.setMinBalance(amount);
    }
}
