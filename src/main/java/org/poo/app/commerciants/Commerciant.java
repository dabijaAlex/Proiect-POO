package org.poo.app.commerciants;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.cashbackStrategies.CashbackStrategy;
import org.poo.app.cashbackStrategies.SpendingThresholdStrategy;
import org.poo.app.cashbackStrategies.nrOfTransactionsCashback;
import org.poo.fileio.CommerciantInput;

import java.util.HashMap;

@Getter @Setter
public class Commerciant {
    protected String commerciant;
    protected int id;
    protected String account;
    protected String type;
    protected CashbackStrategy cashbackStrategy;
    protected double cashbackPercent;
    protected HashMap<Account, Double> listAccountsThatPayedHere =  new HashMap<>();

    public Commerciant(CommerciantInput input, double cashbackPercent) {
        commerciant = input.getCommerciant();
        id = input.getId();
        account = input.getAccount();
        type = input.getType();
        this.cashbackPercent = cashbackPercent;
        if(input.getCashbackStrategy().equals("spendingThreshold"))
            cashbackStrategy = new SpendingThresholdStrategy();
        if(input.getCashbackStrategy().equals("nrOfTransactions"))
            cashbackStrategy = new nrOfTransactionsCashback();
    }

    public void PaymentHappened(double amount, Account account, String currency) {
        cashbackStrategy.addPaymentToMap(this, amount, account, currency);
    }

    public double getCashback(double amount, Account account) {
        return cashbackStrategy.getCashback(this, amount, account);
    }

    public double getCashbackAmount(double amount, Account account) {
        return 0;
    }
}
