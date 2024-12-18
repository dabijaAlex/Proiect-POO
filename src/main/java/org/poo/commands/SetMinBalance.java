package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.fileio.CommandInput;

import java.util.HashMap;

@Getter @Setter
final public class SetMinBalance extends Command {
    HashMap<String, User> users;
    private String IBAN;
    private double amount;
    private int timestamp;


    public SetMinBalance(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.amount = command.getAmount();
        this.timestamp = command.getTimestamp();

        this.users = users;
    }
    public void execute(final ArrayNode output) throws NotFoundException {
        Account acc = getAccountReference(users, IBAN);
        acc.setMinBalance(amount);
    }
}
