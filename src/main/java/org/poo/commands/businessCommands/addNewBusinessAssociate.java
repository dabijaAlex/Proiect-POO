package org.poo.commands.businessCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.HashMap;


@Getter @Setter
public class addNewBusinessAssociate extends Command {
    private HashMap<String, User> users;
    private String accountIban;
    private String role;
    private String email;
    private int timestamp;

    /**
     * Constructor
     * @param input
     * @param users
     */
    public addNewBusinessAssociate(final CommandInput input,
                                   final HashMap<String, User> users) {
        this.cmdName = input.getCommand();
        this.users = users;
        this.accountIban = input.getAccount();
        this.role = input.getRole();
        this.email = input.getEmail();
        this.timestamp = input.getTimestamp();
    }

    /**
     * works for business account only. the rest throw exception
     * @param output
     */
    public void execute(final ArrayNode output) {
        Account account = getAccountReference(users, accountIban);
        User user = getUserReference(users, email);
        account.addBusinessAssociate(role, email, user.getLastName() + " " +  user.getFirstName());
    }
}
