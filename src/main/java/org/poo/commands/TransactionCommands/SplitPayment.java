package org.poo.commands.TransactionCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.ExchangeRateList;
import org.poo.app.NotFoundException;
import org.poo.app.User;
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

    private int timestamp;
    private String description;
    private double amount;
    private List<String> involvedAccounts;
    private String currency;

    /**
     * Constructor
     * @param command
     * @param users user hashmap where all users can be identified by card/ iban / alias/ email
     */
    public SplitPayment(final CommandInput command, final HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.involvedAccounts = command.getAccounts();
        this.timestamp = command.getTimestamp();
        this.currency = command.getCurrency();
        this.total = command.getAmount();

        this.description = "Split payment of " + String.format("%.2f", total) + " " + currency;

        this.users = users;
        this.commandInput = command;
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
        int nrAccounts = involvedAccounts.size();
        amount = total / nrAccounts;
        ArrayList<Double> paymentForEach = new ArrayList<>();
        ArrayList<Account> conturi = new ArrayList<>();

        //  put in an array how much each account should pay based currency
        for (String account : involvedAccounts) {
            Account acc = getAccountReference(users, account);

            double convRate = 1;
            if (!currency.equals(acc.getCurrency())) {
                convRate = ExchangeRateList.convertRate(currency, acc.getCurrency());
            }


            paymentForEach.add(amount * convRate);
            conturi.add(acc);
        }

        //  check they have money;
        for (int i = nrAccounts - 1; i >= 0; i--) {
            //  an account didnt have the money
            if (conturi.get(i).getBalance() < paymentForEach.get(i)) {
                for (int j = 0; j < nrAccounts; j++) {   //poate fac o functie aici
                    conturi.get(j).addTransaction(new SplitPaymentErrorTransaction(timestamp,
                            description, amount, this.involvedAccounts, currency,
                            conturi.get(i).getIBAN()));
                }
                return;
            }
        }

        for (int i = 0; i < nrAccounts; i++) {
            conturi.get(i).setBalance(conturi.get(i).getBalance() - paymentForEach.get(i));
            conturi.get(i).addTransaction(new SplitPaymentTransaction(timestamp, description,
                    amount, involvedAccounts, currency));
        }

    }
}
