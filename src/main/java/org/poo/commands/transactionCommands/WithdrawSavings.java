package org.poo.commands.transactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.notFoundExceptions.NotFoundException;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.NoClassicAccount;
import org.poo.transactions.TooYoungCashWithdrawalTransaction;
import org.poo.transactions.WithdrewSavingsTransaction;

import java.util.HashMap;

@Getter @Setter
public class WithdrawSavings extends Command {
    private static final double REQUIRED_AGE = 21;
    private static final double CURRENT_YEAR = 2024;
    private static final int YEAR_STRING_LENGTH = 4;
    private HashMap<String, User> users;
    private String iban;
    private double amount;
    private String currency;
    private int timestamp;

    /**
     * Constructor
     * @param input
     * @param users
     */
    public WithdrawSavings(final CommandInput input, final HashMap<String, User> users) {
        this.cmdName = input.getCommand();
        this.users = users;
        this.timestamp = input.getTimestamp();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
        this.iban = input.getAccount();
        this.timestampTheSecond = input.getTimestamp();
    }


    /**
     * try and get classic account
     * check age above 21
     * make withdrawal (throws exception if this is not a savings account)
     * @param output
     * @throws NotFoundException
     */
    @Override
    public void execute(final ArrayNode output) throws NotFoundException {
        User user = getUserReference(users, iban);
        Account account = getAccountReference(users, iban);

        Account targetAccount = user.getFirstClassicAccount(currency);
        if (targetAccount == null) {
            //  no classic account
            account.addTransaction(new NoClassicAccount(timestamp));
            return;
        }

        int birthYear = Integer.parseInt(user.getBirthDate().substring(0, YEAR_STRING_LENGTH));
        if (CURRENT_YEAR - birthYear < REQUIRED_AGE) {
            // wrong age output
            targetAccount.addTransaction(new TooYoungCashWithdrawalTransaction(timestamp));
            return;
        }

        Account savingsAccount = getAccountReference(users, iban); // throws acc not found
        savingsAccount.makeWithdrawal(targetAccount, amount); // throws NotASavingsAccount

        savingsAccount.addTransaction(new WithdrewSavingsTransaction(amount,
                targetAccount.getIban(), savingsAccount.getIban(), timestamp));
        targetAccount.addTransaction(new WithdrewSavingsTransaction(amount,
                targetAccount.getIban(), savingsAccount.getIban(), timestamp));
    }
}
