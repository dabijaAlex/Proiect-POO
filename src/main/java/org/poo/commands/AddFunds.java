package org.poo.commands;


import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;

import org.poo.app.*;
import org.poo.fileio.CommandInput;
import java.util.HashMap;


@Getter @Setter
final public class AddFunds extends Command {
    private HashMap<String, User> users;
    private String IBAN;
    private double amount;

    public AddFunds(final CommandInput command, final HashMap<String, User> users) {
        this.IBAN = command.getAccount();
        this.amount = command.getAmount();
        this.users = users;

    }
    public void execute(final ArrayNode output) throws NotFoundException {
        Account cont = getAccountReference(users, IBAN);
        cont.setBalance(cont.getBalance() + amount);
    }
}





