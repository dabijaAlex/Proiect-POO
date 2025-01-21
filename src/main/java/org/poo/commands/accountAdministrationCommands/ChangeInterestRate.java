package org.poo.commands.accountAdministrationCommands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.notFoundExceptions.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.HashMap;


@Getter @Setter
public final class ChangeInterestRate extends Command {
    @JsonIgnore
    private HashMap<String, User> users;

    @JsonProperty("IBAN")
    private String iban;
    private int timestamp;
    private double interestRate;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public ChangeInterestRate(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.iban = command.getAccount();
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
        Account acc = getAccountReference(users, iban);
        acc.setInterestRate(this.interestRate, output, this);
    }
}
