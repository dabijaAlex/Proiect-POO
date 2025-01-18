package org.poo.app.commerciants;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.accounts.Account;
import org.poo.app.cashbackStrategies.CashbackStrategy;
import org.poo.app.cashbackStrategies.SpendingThresholdStrategy;
import org.poo.app.cashbackStrategies.NrOfTransactionsCashback;
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

    /**
     * Constructor
     * @param input
     */
    public Commerciant(final CommerciantInput input) {
        commerciant = input.getCommerciant();
        id = input.getId();
        account = input.getAccount();
        type = input.getType();
        if (input.getCashbackStrategy().equals("spendingThreshold")) {
            cashbackStrategy = new SpendingThresholdStrategy();
        }
        if (input.getCashbackStrategy().equals("nrOfTransactions")) {
            cashbackStrategy = new NrOfTransactionsCashback();
        }
    }

    public void paymentHappened(final double amount, final Account queringAccount,
                                final String currency) {
        cashbackStrategy.addPaymentToMap(this, amount, queringAccount, currency);
    }


    public double getCashback(final double amount, final Account queringAccount) {
        return cashbackStrategy.getCashback(this, amount, queringAccount);
    }

    public double getCashbackAmount(final double amount, final Account queringAccount) {
        return 0;
    }
}
