package org.poo.commands.transactionCommands.splitPayment;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.User;
import org.poo.app.accounts.Account;
import org.poo.transactions.RejectedSplitPaymentTransaction;

import java.util.ArrayList;
import java.util.List;


/**
 * class that keeps how much a single account has to pay and also if it
 * accepted the payment
 */
@Getter @Setter
class Pair {
    private Account account;
    private boolean status;
    private double amountToPay;

    /**
     * constructor
     * @param account
     * @param status
     * @param amountToPay
     */
    Pair(final Account account, final boolean status, final double amountToPay) {
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

    /**
     * constructor
     * @param accounts
     * @param amountEach
     * @param currency
     * @param type
     * @param timestamp
     * @param amount
     * @param description
     * @param involvedAccounts
     * @param amountEachOriginal
     */
    public SingleSplitPayment(final List<Account> accounts, final List<Double> amountEach,
                              final String currency, final String type, final int timestamp,
                              final double amount, final String description,
                              final List<String> involvedAccounts,
                              final List<Double> amountEachOriginal) {
        this.type = type;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
        this.nrAccounts = accounts.size();
        this.involvedAccounts = involvedAccounts;
        this.amountEachOriginal = amountEachOriginal;

        for (int i = 0; i < accounts.size(); i++) {
            Pair pair = new Pair(accounts.get(i), false, amountEach.get(i));
            eachAccountAndPayment.add(pair);
            remainingAccounts.add(accounts.get(i));
        }
    }

    /**
     * search for an account in the list of pairs and set it as accepted
     * @param user
     */
    public void setAcceptedPayment(final User user) {
        for (Pair pair : eachAccountAndPayment) {
            if (pair.getAccount().getUserRef().equals(user)) {
                remainingAccounts.remove(pair.getAccount());
                pair.setStatus(true);
            }
        }
    }

    /**
     * add to all accounts a transaction that the split payment was rejected and remove
     *      the instance from user s pending list
     */
    public void rejected() {
        for (Pair pair : eachAccountAndPayment) {
            Account account = pair.getAccount();
            account.addTransaction(new RejectedSplitPaymentTransaction(timestamp,
                    "Split payment of " + String.format("%.2f", amount)
                            + " " + currency,
                    type, currency, amountEachOriginal, involvedAccounts));
        }
        for (Account account : remainingAccounts) {
            account.getUserRef().getSplitPayments().remove(this);
        }

    }

    /**
     * check if all accounts accepted for an  instance of split payment
     * @return
     */
    public boolean checkAllAccepted() {
        for (Pair pair : eachAccountAndPayment) {
            if (pair.isStatus() == false) {
                return false;
            }
        }
        return true;
    }
}
