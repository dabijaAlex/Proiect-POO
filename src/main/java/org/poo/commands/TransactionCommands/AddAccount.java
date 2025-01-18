package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.NotFoundException;
import org.poo.app.accounts.BusinessAccount;
import org.poo.app.accounts.SavingsAccount;
import org.poo.app.User;
import org.poo.app.plans.ServicePlan;
import org.poo.app.plans.Standard;
import org.poo.app.plans.Student;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.AddAccountTransaction;
import org.poo.utils.Utils;

import java.util.HashMap;

@Getter @Setter
public final class AddAccount extends Command {
    private String email;
    private String currency;
    private String accountType;
    private double interestRate;
    private String iban;
    private int timestamp;
    private String description;

    private HashMap<String, User> users;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public AddAccount(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.email = command.getEmail();
        this.currency = command.getCurrency();
        this.accountType = command.getAccountType();
        this.timestamp = command.getTimestamp();
        this.interestRate = command.getInterestRate();

        this.users = users;

        this.description = "New account created";
    }


    /**
     * get user (if it doesn t exist this will throw
     *
     * check what acc type we want and build one accordingly
     * add this transaction to account
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        //  get user
        User user = getUserReference(users, email);

        //  gen and add IBAN string to HashMap
        iban = Utils.generateIBAN();
        users.put(iban, user);

        ServicePlan servicePlan = user.getServicePlan();

        Account account;
        if(accountType.equals("business")) {
            account = new BusinessAccount(user.getLastName(), user.getEmail(), iban, 0, currency, accountType, servicePlan, user);
            user.addAccount(account);
        } else if (accountType.equals("savings")) {
            account = new SavingsAccount(iban, 0, currency, accountType, interestRate, user, email);
            user.addAccount(account);
        } else {
            account = new Account(iban, 0, currency, accountType, interestRate, user, email);
            user.addAccount(account);
        }

        account.addTransaction(new AddAccountTransaction(this.timestamp));
    }
}
