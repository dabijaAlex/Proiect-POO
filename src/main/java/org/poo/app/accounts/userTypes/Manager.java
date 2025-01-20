package org.poo.app.accounts.userTypes;
import org.poo.app.accounts.Account;
import org.poo.app.commerciants.Commerciant;

import java.util.ArrayList;

public final class Manager extends BAccUser {
    /**
     * constructor that calls superclass
     * @param email
     * @param username
     */
    public Manager(final String email, final String username) {
        super(email, username);
    }

    /**
     * operation not allowed so this throws
     * @param account
     * @param limit
     * @throws ChangeSpendingLimitException
     */
    public void changeSpendingLimit(final Account account,
                                    final double limit) throws ChangeSpendingLimitException {
        throw new ChangeSpendingLimitException();
    }

    /**
     * operation not allowed so this throws
     * @param account
     * @param limit
     * @throws ChangeDepositLimitException
     */
    public void changeDepositLimit(final Account account,
                                   final double limit) throws ChangeDepositLimitException {
        throw new ChangeDepositLimitException();
    }

    /**
     * operation not allowed so this throws
     * @param account
     * @param minBalance
     * @throws NotAuthorizedException
     */
    public void setMinBalance(final Account account,
                              final double minBalance) throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }

    /**
     * action not allowed so this throws
     * @param account
     * @param alias
     * @throws NotAuthorizedException
     */
    public void setAlias(final Account account,
                         final String alias) throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }

    /**
     * take amount from account
     * add this payment to the list of payments made by this user
     * add this payment to the commerciant where it took place
     * @param account
     * @param amount
     * @param commision
     * @param timestamp
     * @param commerciant
     */
    public void makePayment(final Account account, final double amount,
                            final double commision, final int timestamp,
                            final Commerciant commerciant) {
        spent2.add(new Amounts(amount, timestamp));
        double balance = account.getBalance();
        balance = balance - commision;
        balance = balance - amount;
        account.setBalance(balance);
        ArrayList<CommerciantForBusiness> commerciantsForBusinesses =
                account.getCommerciantsForBusiness();
        for (CommerciantForBusiness c : commerciantsForBusinesses) {
            if (commerciant.getCommerciant().equals(c.getName())) {
                c.addManager(this.username);
                c.addAmount(amount);
                return;
            }
        }
        commerciantsForBusinesses.add(new CommerciantForBusiness(commerciant.getCommerciant()));
        commerciantsForBusinesses.getLast().addManager(this.username);
        commerciantsForBusinesses.getLast().addAmount(amount);
    }

    /**
     * add manager to list
     * @param list
     */
    public void addManagerToList(ArrayList<BAccUser> list) {
        list.add(new BAccUser(this));
    }
}
