package org.poo.app.accounts.businessAccount.userTypes;

import org.poo.app.accounts.Account;
import org.poo.app.commerciants.commerciatnTypes.Commerciant;

public final class Owner extends BAccUser {
    /**
     * cinstructor that calls superclass
     * @param email
     * @param username
     */
    public Owner(final String email, final String username) {
        super(email, username);
    }


    /**
     *  make payment. As owner it doesn t need to be accounted for
     * @param account
     * @param amount
     * @param commision
     * @param timestamp
     * @param commerciant
     */
    public void makePayment(final Account account, final double amount, final double commision,
                            final int timestamp, final Commerciant commerciant) {
        double balance = account.getBalance();
        balance = balance - commision;
        balance = balance - amount;
        account.setBalance(balance);
    }

    /**
     * set alias to account
     * @param account
     * @param alias
     */
    public void setAlias(final Account account, final String alias) {
        account.setAlias(alias);
    }

    /**
     * change spending limit for employees
     * @param account
     * @param newLimit
     */
    public void changeSpendingLimit(final Account account, final double newLimit) {
        account.setSpendingLimit(newLimit);
    }

    /**
     * change deposit limit for employees
     * @param account
     * @param newLimit
     */
    public void changeDepositLimit(final Account account, final double newLimit) {
        account.setDepositLimit(newLimit);
    }

    /**
     * set the minimum balance the account can have
     * @param account
     * @param minBalance
     */
    public void setMinBalance(final Account account, final double minBalance) {
        account.setMinBalance(minBalance);
    }
}
