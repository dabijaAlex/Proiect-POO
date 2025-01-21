package org.poo.app.commerciants.cashbackStrategies;

import org.poo.app.accounts.Account;
import org.poo.app.commerciants.commerciatnTypes.Commerciant;



public interface CashbackStrategy {
    /**
     * add a payment or amount spent based on the type of commerciant where the payment happened
     * @param commerciant
     * @param amount
     * @param account
     * @param currency
     */
    void addPayment(Commerciant commerciant, double amount, Account account,
                         String currency);

    /**
     * get cashback based on the type of commerciant where the payment happened
     * @param commerciant
     * @param amount
     * @param account
     * @return
     */
    double getCashback(Commerciant commerciant, double amount, Account account);
}
