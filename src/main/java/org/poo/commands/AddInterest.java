package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.fileio.CommandInput;

import java.util.HashMap;

@Getter @Setter
public class AddInterest extends Command {
    @JsonIgnore
    private HashMap<String, User> users;

    private String IBAN;
    private int timestamp;
//    private String cmdName;


    public AddInterest(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.timestamp = command.getTimestamp();

        this.users = users;
    }
    public void execute(ArrayNode output) throws NotFoundException {
        Account acc = getAccountReference(users, IBAN);

        acc.addInterest(output, this);
    }
}
