package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.SavingsAccount;
import org.poo.app.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.AddAccountTransaction;
import org.poo.utils.Utils;

import java.util.HashMap;

@Getter @Setter
public class AddAccount extends Command {
    private String email;
    private String currency;
    private String accountType;
    private double interestRate;
    private String IBAN;
    private int timestamp;
    private String description;

    private HashMap<String, User> users;

    public AddAccount(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.currency = command.getCurrency();
        this.accountType = command.getAccountType();
        this.timestamp = command.getTimestamp();
        this.interestRate = command.getInterestRate();

        this.users = users;

        this.description = "New account created";
    }



    public void execute(final ArrayNode output) {
        //  get user
        User user = users.get(email);

        //  gen and add IBAN string to HashMap
        IBAN = Utils.generateIBAN();
        users.put(IBAN, user);

        Account account;
        if(accountType.equals("savings")) {
            account = new SavingsAccount(IBAN, 0, currency, accountType);
            user.addAccount(account);
        } else {
            account = new Account(IBAN, 0, currency, accountType);
            user.addAccount(account);
        }

        account.addTransaction(new AddAccountTransaction(this.timestamp));

    }
}
