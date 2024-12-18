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
public class ChangeInterestRate extends Command {
    @JsonIgnore
    private HashMap<String, User> users;

    private String IBAN;
    private int timestamp;
    private double interestRate;

    public ChangeInterestRate(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.IBAN = command.getAccount();
        this.timestamp = command.getTimestamp();
        this.interestRate = command.getInterestRate();

        this.users = users;
    }
    public void execute(ArrayNode output) throws NotFoundException {
        User user = getUserReference(users, IBAN);
        Account acc = getAccountReference(users, IBAN);

        acc.setInterestRate(this.interestRate, output, user, this);
    }
}
