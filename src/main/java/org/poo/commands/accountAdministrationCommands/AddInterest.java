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
public final class AddInterest extends Command {
    @JsonIgnore
    private HashMap<String, User> users;

    @JsonProperty("IBAN")
    private String iban;
    private int timestamp;


    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public AddInterest(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.iban = command.getAccount();
        this.timestamp = command.getTimestamp();
        this.timestampTheSecond = timestamp;

        this.users = users;
    }

    /**
     * Add interest to account if it exists else throw exception
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        Account acc = getAccountReference(users, iban);
        acc.addInterest(output, this);
    }
}
