package org.poo.app.accounts.userTypes;

import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;

public final class Owner extends BAccUser {
    public Owner(final String email, final String username) {
        super(email, username);
    }


    public void makePayment(final Account account, final double amount, final double commision,
                            final int timestamp, final Commerciant commerciant) {
        spent2.add(new Amounts(amount, timestamp));
        double balance = account.getBalance();
        balance = balance - commision;
        balance = balance - amount;
        account.setBalance(balance);
    }

    public void setAlias(final Account account, final String alias) {
        account.setAlias(alias);
    }

    public void changeSpendingLimit(final Account account, final double newLimit) {
        account.setSpendingLimit(newLimit);
    }
    public void changeDepositLimit(final Account account, final double newLimit) {
        account.setDepositLimit(newLimit);
    }
    public void setMinBalance(final Account account, final double minBalance) {
        account.setMinBalance(minBalance);
    }
}
