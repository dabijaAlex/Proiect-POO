package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.SavingsAccount;
import org.poo.app.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class AddAccount extends Command {
    @JsonIgnore
    private HashMap<String, User> users;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String currency;
    @JsonIgnore
    private String accountType;
    @JsonIgnore
    private double interestRate;
    @JsonIgnore
    private String IBAN;


    private int timestamp;
    private String description;
    public AddAccount(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.currency = command.getCurrency();
        this.accountType = command.getAccountType();
        this.timestamp = command.getTimestamp();
        super.timestamp = timestamp;
        this.interestRate = command.getInterestRate();

        this.users = users;

        description = "New account created";
    }



    public void execute(final ArrayNode output) {
        //  get user
        User user = users.get(email);

        //  gen and add IBAN string to HashMap
        IBAN = Utils.generateIBAN();
        users.put(IBAN, user);

        if(accountType.equals("savings"))
            user.addAccount(new SavingsAccount(IBAN, 0, currency, accountType));
        else {
            user.addAccount(new Account(IBAN, 0, currency, accountType));
        }

        user.addTransaction(this);
    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }

}
