package org.poo.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.app.Account;
import org.poo.app.ExchangeRateList;
import org.poo.app.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter @Setter
class SplitPaymentError extends Command {
    private int timestamp;
    private String description;
    private double amount;
    private List<String> involvedAccounts;
    private String currency;
    private String error;

    @JsonIgnore
    private String IBAN;

    public SplitPaymentError(int timestamp, String description, double amount,
                             List<String> involvedAccounts, String currency, String errorAccount, String IBAN) {
        this.timestamp = timestamp;
        super.timestamp = timestamp;
        this.description = description;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
        this.currency = currency;
        this.error = "Account " + errorAccount + " has insufficient funds for a split payment.";

        this.IBAN = IBAN;

    }

    public void addToList(ArrayList<Command> lista) {
//        if(lista.contains(this) == false)
            lista.add(this);
    }
}


@Getter @Setter
public class SplitPayment extends Command {
    @JsonIgnore
    HashMap<String, User> users;
    @JsonIgnore
    private double total;
    @JsonIgnore
    CommandInput commandInput;
    @JsonIgnore
    private String IBAN;
    private int timestamp;
    private String description;
    private double amount;
    private List<String> involvedAccounts;
    private String currency;

    public SplitPayment(CommandInput command, HashMap<String, User> users) {
        this.cmdName = command.getCommand();
        this.involvedAccounts = command.getAccounts();
        this.timestamp = command.getTimestamp();
        this.currency = command.getCurrency();
        this.total = command.getAmount();

        this.description = "Split payment of " + String.format("%.2f", total) + " " + currency;

        this.users = users;
        this.commandInput = command;
    }

    public SplitPayment(CommandInput command, HashMap<String, User> users, String IBAN, double amount) {
        this(command, users);
        this.IBAN = IBAN;
        this.amount = amount;
    }


    public void execute(ArrayNode output) {
        int nrAccounts = involvedAccounts.size();
        amount = total / nrAccounts;
        ArrayList<Double> paymentForEach = new ArrayList<>();
        ArrayList<Account> conturi = new ArrayList<>();
        for(String account : involvedAccounts) {
            User user = users.get(account);
            if(user == null) {
                return;
            }
            Account acc = user.getAccount(account);
            if(acc == null) {
                return;
            }

            double convRate = 1;
            if(currency.equals(acc.getCurrency()) == false) {
                convRate = ExchangeRateList.convertRate(currency, acc.getCurrency());
            }
//            acc.setBalance(acc.getBalance() - amount * convRate);
            paymentForEach.add(amount * convRate);
            conturi.add(acc);
//            user.addTransaction(this);
        }
        //  check they have money;
        for(int i = nrAccounts - 1; i >= 0; i--) {
            if(conturi.get(i).getBalance() < paymentForEach.get(i)) {
//                addTheTransaction(new SplitPaymentError(timestamp, description,amount,
//                        this.involvedAccounts, currency, conturi.get(i).getIBAN()), conturi, nrAccounts);
                for(int j = 0; j < nrAccounts; j++) {
                    SplitPaymentError error = new SplitPaymentError(timestamp, description,amount,
                            this.involvedAccounts, currency, conturi.get(i).getIBAN(), conturi.get(j).getIBAN());
                    User user = users.get(conturi.get(j).getIBAN());
                    if(user == null) {
                        return;
                    }
//                    if(user.getTransactions().contains(error) == false)
                    user.addTransaction(error);
                }
                return;
            }
        }

        for(int i = 0; i < nrAccounts; i++) {
            conturi.get(i).setBalance(conturi.get(i).getBalance() - paymentForEach.get(i));
            User user = users.get(conturi.get(i).getIBAN());
            if(user == null) {
                return;
            }
            user.addTransaction(new SplitPayment(commandInput, users, conturi.get(i).getIBAN(), amount));
        }

    }

    private void addTheTransaction(SplitPaymentError error, ArrayList<Account> conturi, int nrAccounts) {
        for(int i = 0; i < nrAccounts; i++) {
            User user = users.get(conturi.get(i).getIBAN());
            if(user == null) {
                return;
            }
//            if(user.getTransactions().contains(error) == false)
                user.addTransaction(error);
        }
    }

    public void addToList(ArrayList<Command> lista) {
        lista.add(this);
    }

}
