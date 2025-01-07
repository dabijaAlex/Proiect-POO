package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.fileio.CommandInput;

import java.util.HashMap;


@Getter @Setter
public class addNewBusinessAssociate extends Command {
    private HashMap<String, User> users;
    private String account;
    private String role;
    private String email;
    private int timestamp;

    public addNewBusinessAssociate(CommandInput input, HashMap<String, User> users) {
        this.cmdName = input.getCommand();
        this.users = users;
        this.account = input.getAccount();
        this.role = input.getRole();
        this.email = input.getEmail();
        this.timestamp = input.getTimestamp();
    }
}
