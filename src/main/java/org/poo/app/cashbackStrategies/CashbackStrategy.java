package org.poo.app.cashbackStrategies;

import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;
import org.poo.app.commerciants.FoodCommerciant;

public interface CashbackStrategy {
    public void addPaymentToMap(Commerciant commerciant, double amount, Account account, String currency);
    public double getCashback(Commerciant commerciant, double amount, Account account);
}
