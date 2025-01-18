package org.poo.app.cashbackStrategies;

import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;
import org.poo.app.commerciants.FoodCommerciant;

public interface CashbackStrategy {
    public void addPaymentToMap(final Commerciant commerciant, final double amount,
                                final Account account, final String currency);
    public double getCashback(final Commerciant commerciant, final double amount,
                              final Account account);
}
