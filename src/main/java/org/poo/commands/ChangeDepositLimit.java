package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.fileio.CommandInput;

import java.util.HashMap;

@Getter @Setter
public class ChangeDepositLimit extends Command {
    private HashMap<String, User> users;
    private String accountIban;
    private String email;
    private int timestamp;
    private double amount;

    public ChangeDepositLimit(CommandInput input, HashMap<String, User> users) {
        this.cmdName = input.getCommand();
        this.users = users;
        this.accountIban = input.getAccount();
        this.email = input.getEmail();
        this.timestamp = input.getTimestamp();
        this.amount = input.getAmount();
        timestampTheSecond = timestamp;
    }

    public void execute(ArrayNode output) {
        Account account = getAccountReference(users, accountIban);
        account.changeDepositLimit(amount, email);
    }
}
