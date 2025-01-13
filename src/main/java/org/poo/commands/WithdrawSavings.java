package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.*;
import org.poo.app.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transactions.NoClassicAccount;
import org.poo.transactions.TooYoungCashWithdrawalTransaction;
import org.poo.transactions.WithdrewSavingsTransaction;

import javax.security.auth.login.AccountNotFoundException;
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


    @Override
    public void execute(ArrayNode output) throws NotFoundException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", cmdName);
        ObjectNode outputNode = mapper.createObjectNode();


        User user = getUserReference(users, iban);
        Account account = getAccountReference(users, iban);
        int birthYear = Integer.parseInt(user.getBirthDate().substring(0,4));

        Account targetAccount = user.getFirstClassicAccount(currency);
        if(targetAccount == null) {
            //  no classic account
            account.addTransaction(new NoClassicAccount(timestamp));
            return;
        }


        if(2024 - birthYear < 21) {
            // wrong age output
            targetAccount.addTransaction(new TooYoungCashWithdrawalTransaction(timestamp));
            return;

        }

        Account savingsAccount = null;
        try {
            savingsAccount = getAccountReference(users, iban); // throws acc not found
            savingsAccount.makeWithdrawal(targetAccount, amount); // throws NotASavingsAccount
        } catch (NotASavingsAccount e) {
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "This is not a savings account");

            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);
            output.add(objectNode);
            return;
        }
        savingsAccount.addTransaction(new WithdrewSavingsTransaction(amount, targetAccount.getIBAN(), savingsAccount.getIBAN(), timestamp));
        targetAccount.addTransaction(new WithdrewSavingsTransaction(amount, targetAccount.getIBAN(), savingsAccount.getIBAN(), timestamp));


    }
}
