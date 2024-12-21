package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.fileio.CommandInput;

import java.util.HashMap;

@Getter @Setter
public class WithdrawSavings extends Command {
    private HashMap<String, User> users;
    private String iban;
    private double amount;
    private String currency;
    private int timestamp;

    public WithdrawSavings(CommandInput input, HashMap<String, User> users) {
        this.cmdName = input.getCommand();
        this.users = users;
        this.timestamp = input.getTimestamp();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
        this.iban = input.getAccount();
    }
}
