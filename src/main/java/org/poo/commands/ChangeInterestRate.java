package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.fileio.CommandInput;

import java.util.HashMap;


@Getter @Setter
public final class ChangeInterestRate extends Command {
    @JsonIgnore
    private HashMap<String, User> users;

    private String IBAN;
    private int timestamp;
    private double interestRate;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public ChangeInterestRate(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.timestamp = command.getTimestamp();
        this.interestRate = command.getInterestRate();

        this.users = users;
    }

    /**
     * if there no account associated to the IBAN then it will throw exception
     *
     * the setInterestRate method is present in both types of accounts so if it is a normal
     * account, an error will be added to output
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        Account acc = getAccountReference(users, IBAN);
        acc.setInterestRate(this.interestRate, output, this);
    }
}
