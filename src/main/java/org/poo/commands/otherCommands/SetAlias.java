package org.poo.commands.otherCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.HashMap;

@Getter @Setter
public final class SetAlias extends Command {
    private HashMap<String, User> users;
    private String alias;
    private String email;
    private String iban;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public SetAlias(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.alias = command.getAlias();
        this.iban = command.getAccount();

        this.users = users;
    }

    /**
     * set alias to account (if it exists) and then put in the users hashmap the (alias, user) pair
     * so it can later be identified
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {

        User user = getUserReference(users, iban);
        Account cont = getAccountReference(users, iban);

        cont.setAlias(alias);
        users.put(alias, user);
    }
}
