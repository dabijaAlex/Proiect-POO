package org.poo.app.splitPayment;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.ExchangeRateGraph;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.commands.Command;
import org.poo.transactions.RejectedSplitPaymentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Getter @Setter
class Pair {
    private Account account;
    private boolean status;
    private double amountToPay;
    public Pair(Account account, boolean status, double amountToPay) {
        this.account = account;
        this.status = status;
        this.amountToPay = amountToPay;
    }

}


@Getter @Setter
public class SingleSplitPayment {
    @Getter @Setter
    private ArrayList<Pair> eachAccountAndPayment = new ArrayList<Pair>();
    private ArrayList<Account> remainingAccounts = new ArrayList<>();
    private List<String> involvedAccounts;
    private String type;
    private int nrAccounts;
    private int timestamp;
    private String currency;
    private double amount;
    private String description;
    private List<Double> amountEachOriginal;
    public SingleSplitPayment(List<Account> accounts, List<Double> amountEach, String currency, String type, int timestamp, double amount,
                              String description, List<String> involvedAccounts, List<Double> amountEachOriginal) {
        this.type = type;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
        this.nrAccounts = accounts.size();
        this.involvedAccounts = involvedAccounts;
        this.amountEachOriginal = amountEachOriginal;

        for(int i = 0; i < accounts.size(); i++){
            Pair pair = new Pair(accounts.get(i), false, amountEach.get(i));
            eachAccountAndPayment.add(pair);
            remainingAccounts.add(accounts.get(i));
        }
    }

    public Pair findAccount(Account account) {
        for(Pair pair : eachAccountAndPayment){
            if(pair.getAccount().equals(account)){
                remainingAccounts.remove(pair.getAccount());
                return pair;
            }
        }
        return null;
    }

    public void setAcceptedPayment(User user) {
        for(Pair pair : eachAccountAndPayment){
            if(pair.getAccount().getUserRef().equals(user)){
                remainingAccounts.remove(pair.getAccount());
                pair.setStatus(true);
            }
        }
    }

    public void rejected() {
        for(Pair pair : eachAccountAndPayment){
            Account account = pair.getAccount();
            account.addTransaction(new RejectedSplitPaymentTransaction(timestamp,
                    "Split payment of " + String.format("%.2f", amount) + " " + currency,
                    type, currency, amountEachOriginal, involvedAccounts));
        }
        for(Account account : remainingAccounts){
            account.getUserRef().getSplitPayments().remove(this);
        }

    }

    public boolean checkAllAccepted() {
        for(Pair pair : eachAccountAndPayment) {
            if(pair.isStatus() == false)
                return false;
        }
        return true;
    }
    //need to add reference in account
}
