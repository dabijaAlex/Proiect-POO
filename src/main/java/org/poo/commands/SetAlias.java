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
final public class SetAlias extends Command {
    private HashMap<String, User> users;
    private String alias;
    private String email;
    private String IBAN;
    public SetAlias(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.alias = command.getAlias();
        this.IBAN = command. getAccount();

        this.users = users;
    }
    public void execute(final ArrayNode output) throws NotFoundException {

        User user = getUserReference(users, IBAN);
        Account cont = getAccountReference(users, IBAN);

        cont.setAlias(alias);
        users.put(alias, user);
    }
}
