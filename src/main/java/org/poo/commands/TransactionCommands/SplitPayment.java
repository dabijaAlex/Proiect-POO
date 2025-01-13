package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.NotFoundException;
import org.poo.app.User;
import org.poo.app.splitPayment.SingleSplitPayment;
import org.poo.app.splitPayment.SplitPaymentDB;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.SplitPaymentErrorTransaction;
import org.poo.transactions.SplitPaymentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Getter @Setter
public class SplitPayment extends Command {
    private HashMap<String, User> users;
    private double total;
    private CommandInput commandInput;
    private String type;

    private int timestamp;
    private String description;
    private double amount;
    private List<String> involvedAccounts;
    private List<Account> involvedAccountsRef = new ArrayList<>();
    private String currency;
    private List<Double> amountForUsers;
    private List<Double> amountForUsersOriginal = new ArrayList<>();

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public SplitPayment(final CommandInput command, final HashMap<String, User> users) {
        this.type = command.getSplitPaymentType();
        this.cmdName = command.getCommand();
        this.users = users;
        this.involvedAccounts = command.getAccounts();
        this.timestamp = command.getTimestamp();

        this.amount = command.getAmount();
        this.currency = command.getCurrency();
        this.description = "Split payment of " + String.format("%.2f", amount) + " " + currency;

        if(type.equals("custom")) {
            this.amountForUsers = command.getAmountForUsers();
            for (Double amountForUser : amountForUsers) {
                this.amountForUsersOriginal.add(amountForUser);
            }
        } else {
            amountForUsers = new ArrayList<>();
            double nrAccounts = command.getAccounts().size();
            double amountEach = amount / nrAccounts;
            for(int i = 0; i < nrAccounts; i++) {
                amountForUsers.add(amountEach);
            }
        }
    }

    private void convertForCurrency() {
        for(int i = 0; i < amountForUsers.size(); i++) {
            Double amountEach = amountForUsers.get(i);
            amountEach = ExchangeRateGraph.makeConversion(currency, involvedAccountsRef.get(i).getCurrency(), amountEach);
            amountForUsers.set(i, amountEach);
        }
    }


    /**
     * put in an array how much each account should pay based currency
     *
     * check each account has the required amount
     * if an account didn t have the money add a failed transaction to all accounts and return
     *
     * if all accounts had enough money then subtract the amount for each and add transaction
     *
     * @param output
     * @throws NotFoundException
     */
    public void execute(final ArrayNode output) throws NotFoundException {
        for(String accountIban : involvedAccounts) {
            Account acc = getAccountReference(users, accountIban);
            this.involvedAccountsRef.add(acc);
        }

        this.convertForCurrency();
        SingleSplitPayment payment = new SingleSplitPayment(involvedAccountsRef, amountForUsers, currency, type,
                timestamp, amount, description, involvedAccounts, amountForUsersOriginal);
        for(String accountIban : involvedAccounts) {
            User user = getUserReference(users, accountIban);
            user.addSplitPayment(payment);
        }


        SplitPaymentDB.addSplitPaymentToList(payment);
    }

    }